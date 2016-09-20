package com.ip.music_portal.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userManager")
public interface IUserManager extends RemoteService {

	boolean addUser(User user);

	void modifyUser(User oldUser, User newUser);

	boolean deleteUser(String userName);
	
	boolean containsUser(User user);
	
	boolean containsUserWithUserName(String userName);
	
	int userCount();
	
	User getUserByUserName(String userName);
	
	List<String> getAllUserNames();

}
