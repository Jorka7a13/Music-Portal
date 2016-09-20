package com.ip.music_portal.classes;

import com.ip.music_portal.client.Comment;
import com.ip.music_portal.client.Upload;
import com.ip.music_portal.client.User;
import com.ip.music_portal.interfaces.IUserService;
import com.ip.music_portal.server.UserManagerImpl;

public class UserService extends UserManagerImpl implements IUserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1109437581275059169L;

	@Override
	public void login(String userName_, String password_) {

		User user = getUserByUserName(userName_);

		if (userName_.equals(user.getUserName())) {
			if (password_.equals(user.getPassword())) {
				user.setLoggedIn(true);
			} else {
				System.out.println("Wrong password!");
			}
		} else {
			System.out.println("Wrong username!");
		}
	}

	@Override
	public void addComment(Comment comment) {

	}

	@Override
	public void rateUpload(Upload upload, int rating) {

	}

	@Override
	public void logoutByUserName(String userName_) {
		User user = getUserByUserName(userName_);
		user.setLoggedIn(false);
	}

	@Override
	public boolean isLogged(String userName_) {
		User user = getUserByUserName(userName_);
		return (user.isLoggedIn());
	}

}
