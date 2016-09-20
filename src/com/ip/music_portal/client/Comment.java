package com.ip.music_portal.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Comment implements Serializable {

	private static final long serialVersionUID = 6660677725200452845L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;
	
	@Persistent
	private String author;
	
	@Persistent
	private String text;
	
	@Persistent
	private String uploadName;
	
	public Comment() {}
	
	public Comment(String uploadName_, String author_, String text_) {
		this.uploadName = uploadName_;
		this.author = author_;
		this.text = text_;
	}
	
	public String getAuthor() {
		return this.author;
	}

	public String getText() {
		return this.text;
	}
	
	public Long getKey() {
		return this.key;
	}
	
	public String getUploadName() {
		return this.uploadName;
	}
	
}
