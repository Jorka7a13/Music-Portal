package com.ip.music_portal.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ip.music_portal.classes.Search;
import com.ip.music_portal.client.User;

public class SearchTest {

	@Test
	public void searchUser() {

		Search search = new Search();
		User user1 = new User("IchoKatsarski", "1234", "icho@gmail.com");
		User user2 = new User("Tochkata", "4321", "alabala@gmail.com");
		User user3 = new User("Nedomir", "ne" , "bonecrusher@abv.bg");
		search.addUser(user1);
		search.addUser(user2);
		search.addUser(user3);

		assertEquals(user1, search.searchUser("IchoKatsarski"));
		assertEquals(user2, search.searchUser("Tochkata"));
		assertEquals(user3, search.searchUser("Nedomir"));
	}


/*	@Test
	public void searchTrack() {

		Map<String, String> track = new HashMap<String, String>();
		track.put("Skrillex", "Cinema");
		track.put("Nero", "Innocence");

		String target = "Nero";
		assertTrue(track.containsKey(target) || track.containsValue(target));

	}

	@Test
	public void searchGenre() {
		List<String> genres = new ArrayList<String>(); // List of the genres
		genres.add("Dubstep");
		genres.add("HipHop");

		Map<String, String> music = new HashMap<String, String>(); // Map of the
																	// artists
																	// and the
																	// genre
																	// they play
		music.put("Skrillex", "Dubstep");
		music.put("Logo5", "HipHop");

		String target = "Dubstep";

		// assertTrue(genres.contains(music.containsValue(target)));
		assertEquals(music.containsValue(target), genres.contains(target));

	}
*/
}
