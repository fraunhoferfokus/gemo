package de.fhg.fokus.data;

public interface UserDAO {
	User getUser(String username);

	void saveUser(User user);

	boolean doesUserExist(User user);

	boolean isValidUser(String openid, String password);
}
