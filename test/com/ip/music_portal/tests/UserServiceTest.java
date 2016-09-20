package com.ip.music_portal.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ip.music_portal.classes.UserService;
import com.ip.music_portal.client.User;

public class UserServiceTest {

	@Test
	public void isLoggedTest() {
		UserService userService = new UserService();
		User user = new User("jorkata", "1111", "some@email.com");

		userService.addUser(user);
		userService.login("jorkata", "1111");

		assertTrue(userService.isLogged("jorkata"));
	}

	@Test
	public void loginTest() {

		UserService userService = new UserService();

		User user = new User("jorkata", "1111", "some@email.com");

		userService.addUser(user);

		userService.login("jorkata", "1111");

		assertTrue(user.isLoggedIn());
	}

	@Test
	public void logoutByUserNameTest() {

		UserService userService = new UserService();

		User user = new User("jorkata", "1111", "some@email.com");

		userService.addUser(user);

		userService.login("jorkata", "1111");

		assertTrue(user.isLoggedIn());

		userService.logoutByUserName("jorkata");

		assertFalse(user.isLoggedIn());
	}

}
