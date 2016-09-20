package com.ip.music_portal.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ip.music_portal.client.IUserManager;
import com.ip.music_portal.client.User;

public class UserManagerImpl extends RemoteServiceServlet implements IUserManager {

	private static final long serialVersionUID = -5018307716273555272L;
	
	PersistenceManager pm;
	
	@Override
	public boolean addUser(User user) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {
			
			Query query = pm.newQuery(User.class);
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute();

			List<String> returnList = new ArrayList<String>();
			
			for(User currentUser : results) {
				returnList.add(currentUser.getUserName());
			}
			
			if(!returnList.contains(user.getUserName())) {
				pm.makePersistent(user);
				return true;
			} else {
				System.out.println("This user [" + user.getUserName() + "] already exists!");
				return false;
			}
			
	    } finally {
	        pm.close();
	    }
		
	}

	@Override
	public void modifyUser(User oldUser, User newUser) {  //Need implementation for later use
		/*if (userList.contains(oldUser)) {
			int index = userList.indexOf(oldUser);
			userList.remove(oldUser);
			userList.add(index, newUser);
		} else {
			System.out.println("This user [" + oldUser.getUserName()
					+ "] doesn't exist!");
			throw new NullPointerException();
		}*/
		
	}

	@Override
	public boolean deleteUser(String userName) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(User.class);
			query.setFilter("userName == userNameParam");
			query.declareParameters("String userNameParam");
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute(userName);
			
			if(results.size()>0) {
				query.deletePersistentAll(userName);
				return true;
			}
			
			return false;
			
		} finally {
	        pm.close();
	    }
		
	}

	@Override
	public boolean containsUser(User user) {
	
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(User.class);
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute();

			List<User> returnList = new ArrayList<User>();
						
			for(User currentUser : results) {
				returnList.add(currentUser);
			}
			
			if(returnList.contains(user)) {
				return true;
			} else {
				return false;
			}
			
		} finally {
	        pm.close();
	    }
		
	}

	@Override
	public int userCount() {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(User.class);
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute();

			List<String> returnList = new ArrayList<String>();
						
			for(User currentUser : results) {
				returnList.add(currentUser.getUserName());
			}
			
			return returnList.size();
			
		} finally {
	        pm.close();
	    }

	}

	@Override
	public User getUserByUserName(String userName_) {

		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(User.class);
			query.setFilter("userName == userNameParam");
			query.declareParameters("String userNameParam");
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute(userName_);
	
			if(results.size() > 1) {
				System.out.println("There are more than one user with that username - [" + userName_ + "]!!!");
			} else if(results.size() == 1) {
				return results.get(0);
			} else if(results.size() == 0) {
				System.out.println("There is no user with username - [" + userName_ + "]!!!");
			}
			
			return null;
			
		} finally {
	        pm.close();
	    }
		
	}
	
	@Override
	public boolean containsUserWithUserName(String userName_) {

		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(User.class);
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute();

			List<String> returnList = new ArrayList<String>();
						
			for(User currentUser : results) {
				returnList.add(currentUser.getUserName());
			}
			
			if(returnList.contains(userName_)) {
				return true;
			} else {
				return false;
			}
			
		} finally {
	        pm.close();
	    }

	}

	@Override
	public List<String> getAllUserNames() {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(User.class);
			
			@SuppressWarnings("unchecked")
			List<User> results = (List<User>) query.execute();

			List<String> returnList = new ArrayList<String>();
			
			for(User user : results) {
				returnList.add(user.getUserName());
			}
			
			return returnList;
			
		} finally {
	        pm.close();
	    }
		
	}
	
	

}
