package hashbrowns.p1.orm.mapper;

import java.util.List;

public interface OrmService {

	/**
	 * 
	 * @param table (String) The database table used for query building.
	 * @param object (Object) The object passed into the ORM, modified then returned.
	 * @return Object(s) with set fields that were retrieved from database queries
	 * @throws Exception
	 */
	Object selectObjId(String table, Object object) throws Exception;

	/**
	 * 
	 * @param table
	 * @param object
	 * @return
	 */
	Object insertObj(String table, Object object);

	/**
	 * 
	 * @param table
	 * @param object
	 * @return
	 * @throws Exception
	 */
	Object updateObj(String table, Object object) throws Exception;

	/**
	 * 
	 * @param table
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	Object deleteObj(String table, Object object) throws IllegalArgumentException, IllegalAccessException;

	/**
	 * 
	 * @param <T>
	 * @param table
	 * @param object
	 * @return
	 * @throws Exception
	 */
	<T> List<T> selectAllSQL(String table, Object object) throws Exception;


	
}
