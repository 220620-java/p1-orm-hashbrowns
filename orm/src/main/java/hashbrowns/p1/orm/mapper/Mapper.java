package hashbrowns.p1.orm.mapper;

import java.util.List;

public interface Mapper {

	Object selectObjId(String table, Object object) throws Exception;

	Object insertObj(String table, Object object);

	Object updateObj(String table, Object object) throws Exception;

	Object deleteObj(String table, Object object) throws IllegalArgumentException, IllegalAccessException;

	<T> List<T> selectAllSQL(String table, Object object) throws Exception;


	
}
