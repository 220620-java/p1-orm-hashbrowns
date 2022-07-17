package hashbrowns.p1.orm.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

import hashbrowns.p1.orm.utils.ConnectDB;
import hashbrowns.p1.orm.utils.Logger;
import hashbrowns.p1.orm.utils.LoggingLevel;
import hashbrowns.p1.orm.annotations.id;
import hashbrowns.p1.orm.annotations.ignore;

public class OrmExec implements OrmService {
	//
	private static Logger logger = Logger.getLogger();
	private ConnectDB connUtil = ConnectDB.getConnectionDB();

	@Override
	public Object insertObj(String table, Object object) {
		//
		StringJoiner comma1 = new StringJoiner(", ");
		StringJoiner comma2 = new StringJoiner("', '");
		//
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Stream<Field> strArray = Arrays.stream(fields);
		//
		strArray.forEach(field -> {
			field.setAccessible(true);
			try {
				if (!field.get(object).equals(null) & !field.isAnnotationPresent(ignore.class)
						& !field.isAnnotationPresent(id.class)) {

					comma1.add(field.getName());
					comma2.add(field.get(object).toString());
				} else if (field.isAnnotationPresent(id.class)) {
				}
			} catch (Exception e) {
				logger.log("Nulled fields are being excluded from the statement", LoggingLevel.INFO);
			}
		});
		//
		String query = "INSERT INTO " + table + "(" + comma1.toString() + ") VALUES ('" + comma2.toString() + "')";

		try (Connection conn = connUtil.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return object;
	}

	@Override
	public Object selectObjId(String table, Object object) throws Exception {
		//
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String id = null;
		String idValue = null;
		//
		for (Field field : fields) {
			if (field.isAnnotationPresent(id.class)) {
				field.setAccessible(true);
				id = field.getName().toString();
				idValue = field.get(object).toString();
			}
		}

		//
		String query = "SELECT * FROM " + table + " WHERE " + id + " = '" + idValue + "'";

		try (Connection conn = connUtil.getConnection()) {
			Field[] fields1 = object.getClass().getDeclaredFields();
			Map<String, Object> row = new HashMap<String, Object>();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet resultSet = stmt.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columns = metaData.getColumnCount();
			//
			while (resultSet.next()) {
				for (int i = 1; i <= columns; i++) {
					row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
				}
				//
				row.entrySet().stream().forEach(e -> {
					for (Field field : fields1) {
						field.setAccessible(true);
						try {
							if (field.getName().equals(e.getKey()) && !field.isAnnotationPresent(ignore.class)
									&& field.getType().getSimpleName().equals("String")) {
								field.set(object, e.getValue().toString());
							} else if (field.getName().equals(e.getKey()) && !field.isAnnotationPresent(ignore.class)
									&& field.getType().getSimpleName().equals("int")) {
								field.setInt(object, (int) e.getValue());
							} else if (field.getName().equals(e.getKey()) && !field.isAnnotationPresent(ignore.class)
									&& field.getType().getSimpleName().equals("Double")) {
								double d = Double.parseDouble((String) e.getValue());
								field.set(object, d);
							} else if (field.getName().equals(e.getKey()) && !field.isAnnotationPresent(ignore.class)
									&& field.getType().getSimpleName().equals("boolean")) {
								field.setBoolean(object, (boolean) e.getValue());
							} else {

							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public Object updateObj(String table, Object object) throws Exception {
		//
		StringBuilder fieldStr = new StringBuilder();
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		//
		String id = null;
		String idValue = null;
		//
		for (Field field : fields) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(id.class) && !field.isAnnotationPresent(ignore.class)
					&& (field.get(object) != null)) {
				fieldStr.append(field.getName());
				fieldStr.append("='");
				fieldStr.append(field.get(object));
				fieldStr.append("', ");
			} else if (field.isAnnotationPresent(id.class)) {
				field.setAccessible(true);
				id = field.getName().toString();
				idValue = field.get(object).toString();
			}
		};
		fieldStr.setLength(fieldStr.length() - 2);
		//
		String query = "UPDATE " + table + " SET " + fieldStr.toString() + " where " + id + "=" + idValue + "";
		try (Connection conn = connUtil.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public Object deleteObj(String table, Object object) throws IllegalArgumentException, IllegalAccessException {
		//
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String id = null;
		String idValue = null;
		//
		for (Field field : fields) {
			if (field.isAnnotationPresent(id.class)) {
				field.setAccessible(true);
				id = field.getName().toString();
				idValue = field.get(object).toString();
			}
		}
		//
		String sql = "DELETE FROM " + table + " WHERE " + id + "='" + idValue + "';";
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public <T> List<T> selectAllSQL(String table, Object object) throws Exception {
		String sql = "SELECT * FROM " + table;
		List<T> all = new ArrayList<>();
		try (Connection conn = connUtil.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs;
			Class<?> clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			rs = ps.executeQuery();
			while (rs.next()) {
				T temp = (T) object.getClass().getConstructor().newInstance();
				for (Field field : fields) {
					field.setAccessible(true);
					Annotation annId = field.getAnnotation(id.class);
					annId = field.getAnnotation(id.class);
					if (annId == null) {
						int column = rs.findColumn(field.getName());
						if (field.getType().getSimpleName().equals("String")) {
							field.set(temp, rs.getObject(column));
						} else if (field.getType().getSimpleName().equals("int")) {
							field.setInt(temp, (int) rs.getObject(column));
						} else if (field.getType().getSimpleName().equals("Double")) {
							double d = Double.parseDouble((String) rs.getObject(column));
							field.set(temp, d);
						} else if (field.getType().getSimpleName().equals("boolean")) {
							field.setBoolean(temp, (boolean) rs.getObject(column));
						}
					} else {
						int column = rs.findColumn(field.getName());
						field.set(temp, rs.getObject(column));
					}
				}
				all.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return all;
	}
}
