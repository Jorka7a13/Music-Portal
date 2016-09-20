package com.ip.music_portal.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUserManagerAsync {

	void addUser(User user, AsyncCallback<Boolean> callback);

	void modifyUser(User oldUser, User newUser, AsyncCallback<Void> callback);

	void deleteUser(String userName, AsyncCallback<Boolean> callback);
	
	void containsUser(User user, AsyncCallback<Boolean> callback);
	
	void containsUserWithUserName(String userName, AsyncCallback<Boolean> callback);
	
	void userCount(AsyncCallback<Integer> callback);
	
	void getUserByUserName(String userName, AsyncCallback<User> callback);
	
	void getAllUserNames(AsyncCallback<List<String>> callback);
	
}
