package com.crypto.dao;

import java.util.List;

import com.crypto.DataStore;
import com.crypto.entities.User;

public class UserDao {

	public List<User> getUsers() {

		return DataStore.getUsers();
	}

	public User getUser(long userId) {
		return DataStore.getUser(userId);
	}

	public long authenticate(String email, String password) {
		return DataStore.authenticate(email, password);
	}
}
