package controllers;

import models.User;

public class Security {

	static boolean authenticate(String username, String password) {
		return User.connect(username, password) != null;
	}
}
