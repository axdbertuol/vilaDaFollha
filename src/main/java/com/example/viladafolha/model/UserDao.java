package com.example.viladafolha.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao {

	private static final Map<String, User> db = new HashMap<>();

	public UserDao() {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		User user = new User("alexandre.bertuol@gmail.com", pe.encode("123456"));
		db.put(user.getEmail(), user);
	}

	public User getUser(String email) {
		return db.get(email);
	}



}