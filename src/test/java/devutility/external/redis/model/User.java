package devutility.external.redis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private int id;
	private String name;
	private int age;

	@JsonProperty("Countries")
	private String[] countries;

	private Date birthday;

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

	@JsonIgnore
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String[] getCountries() {
		return countries;
	}

	public void setCountries(String[] countries) {
		this.countries = countries;
	}

	public static List<User> list(int count) {
		List<User> list = new ArrayList<>(count);
		String[] countries = { "us", "uk", "cn" };

		for (int i = 0; i < count; i++) {
			User user = new User();
			user.setId(1000 + i + 1);
			user.setAge(i + 20);
			user.setBirthday(new Date());
			user.setCountries(countries);
			user.setName(String.format("Name_%s", i + 1));
			list.add(user);
		}

		return list;
	}

	public static String[][] getArrays(int count) {
		List<User> list = User.list(count);
		return toArrays(list);
	}

	private static String[][] toArrays(List<User> users) {
		List<String[]> list = new LinkedList<>();

		for (User user : users) {
			list.add(toArray(user));
		}

		return list.toArray(new String[0][]);
	}

	private static String[] toArray(User user) {
		String[] array = new String[3];
		array[0] = user.getName();
		array[1] = String.valueOf(user.getBirthday().getTime());
		array[2] = String.valueOf(user.getId());
		return array;
	}
}