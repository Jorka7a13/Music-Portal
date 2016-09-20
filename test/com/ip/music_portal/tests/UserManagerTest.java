package com.ip.music_portal.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ip.music_portal.client.User;
import com.ip.music_portal.server.UserManagerImpl;

public class UserManagerTest {

	@Test
	public void addUserTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		userManager.addUser(user1);

		assertEquals(1, userManager.userCount());
	}

	@Test
	public void modifyUserTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		User user2 = new User("jorkata2", "2222", "test2@gmail.com");

		userManager.addUser(user1);
		userManager.modifyUser(user1, user2);

		assertFalse(userManager.containsUser(user1));
		assertTrue(userManager.containsUser(user2));
	}

	@Test
	public void deleteUserTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		User user2 = new User("jorkata2", "2222", "test2@gmail.com");
		userManager.addUser(user1);
		userManager.addUser(user2);

		userManager.deleteUser(user2.getUserName());

		assertEquals(1, userManager.userCount());
	}

	@Test
	public void containsUserTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		userManager.addUser(user1);

		assertTrue(userManager.containsUser(user1));
	}
	
	@Test
	public void containsUserWithUserNameTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		userManager.addUser(user1);

		assertTrue(userManager.containsUserWithUserName("jorkata"));
	}

	@Test
	public void userCountTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		userManager.addUser(user1);

		assertEquals(1, userManager.userCount());
	}

	@Test
	public void getUserByUserNameTest() {
		UserManagerImpl userManager = new UserManagerImpl();
		User user1 = new User("jorkata", "1111", "test@gmail.com");
		userManager.addUser(user1);

		assertEquals(user1, userManager.getUserByUserName("jorkata"));
	}

}
