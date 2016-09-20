package com.ip.music_portal.interfaces;

import com.ip.music_portal.client.Upload;
import com.ip.music_portal.client.User;

public interface ISearch {

	public User searchUser(String UserName);

	public Upload searchUpload(String UploadName);
	
	public Upload searchByGenre(String Genres); //List<String>Genres
			
}
