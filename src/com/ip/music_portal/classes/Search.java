package com.ip.music_portal.classes;

import com.ip.music_portal.client.Upload;
import com.ip.music_portal.client.User;
import com.ip.music_portal.interfaces.ISearch;
import com.ip.music_portal.server.UserManagerImpl;

public class Search extends UserManagerImpl implements ISearch {

	@Override
	public User searchUser(String UserName) {

		//User currentUser = getFirstUser();

		for (int i = 0; i < userCount(); i++) {

		//	if (UserName.equals(currentUser.getUserName())) {
		//		return currentUser;
			}
		//	currentUser = getNextUser(currentUser);

		//}

		System.out.println("User Not Found");

		throw new NullPointerException();

		/* С Итератор 
		 * Iterator<User> iterator = userList.iterator();
		 * 
		 * while (iterator.hasNext()) { User currentUser = iterator.next(); if
		 * (UserName.equals(currentUser.getUserName())) { return currentUser; }
		 * }
		 * 
		 * System.out.println("User Not Found");
		 * 
		 * throw new NullPointerException();
		 */
	}

	@Override
	public Upload searchUpload(String UploadName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Upload searchByGenre(String Genres) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		new Search();
	}

}
