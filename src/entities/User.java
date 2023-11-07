package entities;

public class User {
	private final String username;
	private final String city;
	private final Integer age;



	public User(String username, String city, Integer age) {
		this.username = username;
		this.city = city;
		this.age = age;
	}

	public String getUsername() {
		return username;
	}
	public String getCity() {
		return city;
	}
	public Integer getAge() {
		return age;
	}
}
