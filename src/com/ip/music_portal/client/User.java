package com.ip.music_portal.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User implements Serializable {

	private static final long serialVersionUID = -6144760089095779609L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private String userName = null; 
	
	@Persistent
	private String password = null;
	
	@Persistent
	private String email = null;
	
	@Persistent
	private String bio = null; // optional
	
	@Persistent
	private UserStatus userStatus = null;
	
	@NotPersistent
	private boolean loggedIn = false;
	
	public User() {}
	
	public User(String userName_, String password_, String email_) {
		this.userName = userName_;
		this.password = password_;
		this.email = email_;
		this.userStatus = UserStatus.REGULAR;
	}

	public Long getKey() {
		return key;
	}
	
	public void setBio(String bio_) {
		this.bio = bio_;
	}
	
	public String getBio() {
		return this.bio;
	} 
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setLoggedIn(boolean isLogged_) {
		this.loggedIn = isLogged_; 
	}
	
	public boolean isLoggedIn() {
		return this.loggedIn;
	}
	
	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public enum UserStatus {REGULAR, ADMIN}

}
