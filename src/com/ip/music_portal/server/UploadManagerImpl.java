package com.ip.music_portal.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ip.music_portal.client.IUploadManager;
import com.ip.music_portal.client.Upload;

public class UploadManagerImpl extends RemoteServiceServlet implements IUploadManager {

	private static final long serialVersionUID = 1549884380025654685L;
	
	PersistenceManager pm;

	@Override
	public void addUpload(Upload upload) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {
			pm.makePersistent(upload);
	    } finally {
	        pm.close();
	    }

	}

	@Override
	public boolean deleteUpload(String uploadName) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Upload.class);
			query.setFilter("name == nameParam");
			query.declareParameters("String nameParam");
			
			@SuppressWarnings("unchecked")
			List<Upload> results = (List<Upload>) query.execute(uploadName);
			
			if(results.size()>0) {
				query.deletePersistentAll(uploadName);
				return true;
			}
			
			return false;
			
		} finally {
	        pm.close();
	    }
	}

	@Override
	public boolean containsUploadWithName(String uploadName) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Upload.class);
			
			@SuppressWarnings("unchecked")
			List<Upload> results = (List<Upload>) query.execute();

			List<String> returnList = new ArrayList<String>();
						
			for(Upload currentUpload : results) {
				returnList.add(currentUpload.getName());
			}
			
			if(returnList.contains(uploadName)) {
				return true;
			} else {
				return false;
			}
			
		} finally {
	        pm.close();
	    }

	}

	@Override
	public int uploadCount() {

		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Upload.class);
			
			@SuppressWarnings("unchecked")
			List<Upload> results = (List<Upload>) query.execute();

			List<String> returnList = new ArrayList<String>();
						
			for(Upload currentUpload : results) {
				returnList.add(currentUpload.getName());
			}
			
			return returnList.size();
			
		} finally {
	        pm.close();
	    }
		
	}

	@Override
	public Upload getUploadByName(String uploadName) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Upload.class);
			query.setFilter("name == nameParam");
			query.declareParameters("String nameParam");
			
			@SuppressWarnings("unchecked")
			List<Upload> results = (List<Upload>) query.execute(uploadName);
	
			if(results.size() > 1) {
				System.out.println("There are more than one uploads with name [" + uploadName + "]!!!");
			} else if(results.size() == 1) {
				return results.get(0);
			} else if(results.size() == 0) {
				System.out.println("There is no upload with username [" + uploadName + "]!!!");
			}
			
			return null;
			
		} finally {
	        pm.close();
	    }
		
	}

	@Override
	public List<String> getAllUploadNames() {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Upload.class);
			
			@SuppressWarnings("unchecked")
			List<Upload> results = (List<Upload>) query.execute();

			List<String> returnList = new ArrayList<String>();
			
			for(Upload currentUpload : results) {
				returnList.add(currentUpload.getName());
			}
			
			return returnList;
			
		} finally {
	        pm.close();
	    }
		
	}
	
}
