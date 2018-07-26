package com.github.hemantsonu20.cric.model;

public class FirebaseUser extends BaseBean {

	private final String uid;
	private final String email;
	private final String name;

	public FirebaseUser(String uid, String email, String name) {
		this.uid = uid;
		this.email = email;
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}
}
