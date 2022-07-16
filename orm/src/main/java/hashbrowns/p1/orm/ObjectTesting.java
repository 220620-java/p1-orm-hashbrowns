package hashbrowns.p1.orm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import hashbrowns.p1.orm.mapper.*;
import hashbrowns.p1.orm.utils.Logger;
import hashbrowns.p1.orm.utils.LoggingLevel;

public class ObjectTesting {
	private static Logger logger = Logger.getLogger();
	
	public static void main(String[] args) throws Exception {
		
		// Instantiate the ORM and Test Model
		Orm orm = new Orm();
		
		
		
		TestModel testObj = new TestModel();
		
		// For Inserting and Update Testing
		//--
		//testObj.setName("Prince Fielder");
		//testObj.setPosition("1B/DH");
		//testObj.setDebut("06/16/2005");
		//testObj.setAverage(0.348);
		//testObj.setHomeruns(14);
		//testObj.setRbi(42);
		//testObj.setActive(false);
		
		
		// For Retrieval Resting
		testObj.setId(5);
		
		
		// Test Method and Return Object
		Object returnObj  = orm.selectObjId("players.player", testObj);

		// JSON testing
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(returnObj);
		
		
		// Out the results
		System.out.println(json);
		System.out.println(returnObj);
		
		
		logger.log("-- Object Testing End --", LoggingLevel.INFO);
		
		
		// ORM Methods ("table", object)
		//-------------------------------------------
		//orm.insertObj("players.player", testObj);
		//orm.selectObjId("players.player", testObj);
		//orm.selectAllSQL("players.player", testObj);
		//orm.updateObj("players.player", testObj);
		//orm.deleteObj("players.player", testObj);
	}

}