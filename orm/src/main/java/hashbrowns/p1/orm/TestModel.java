package hashbrowns.p1.orm;

import java.util.ArrayList;

public class TestModel {
	private int id;
	private String name;
	private String username;
	private String password;
	private ArrayList<?> recipes = new ArrayList<>();

	public TestModel() {
		this.id = 3;
		this.name = "Chef Ramsay";
		this.username = "testuser";
		this.password = "testpass";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<?> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<?> recipes) {
		this.recipes = recipes;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}