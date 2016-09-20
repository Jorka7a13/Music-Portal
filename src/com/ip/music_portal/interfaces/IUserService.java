package com.ip.music_portal.interfaces;

import com.ip.music_portal.client.Comment;
import com.ip.music_portal.client.Upload;

public interface IUserService {

	public void login(String name, String password);

	public void addComment(Comment comment);

	public void rateUpload(Upload upload, int rating);

	public void logoutByUserName(String name);

	public boolean isLogged(String userName);

}
