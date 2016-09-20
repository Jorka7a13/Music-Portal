package com.ip.music_portal.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Upload implements Serializable {
	
	private static final long serialVersionUID = 8067910349705268178L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String uploadersName;
	
	@Persistent
	private int rating;
	
	@Persistent
	private String urlToFile;
	
	public Upload() {}
	
	public Upload(String name_, String uploadersName_, String url_) {
		this.name = name_;
		this.uploadersName = uploadersName_;
		this.urlToFile = url_;
	}
	
	public Long getKey() {
		return key;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getUploaderName() {
		return this.uploadersName;
	}
	
	public String getUrlToFile() {
		return urlToFile;
	}
	
	public int getRating() {
		return this.rating;
	}
	
	public void setRating(int rating_) {
		this.rating = rating_;
	}

	
}
