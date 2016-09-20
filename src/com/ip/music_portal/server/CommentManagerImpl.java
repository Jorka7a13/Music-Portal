package com.ip.music_portal.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ip.music_portal.client.Comment;
import com.ip.music_portal.client.ICommentManager;

public class CommentManagerImpl extends RemoteServiceServlet implements ICommentManager {

	private static final long serialVersionUID = 2704134129679518850L;

	PersistenceManager pm;
	
	@Override
	public void addComment(Comment comment) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {			
			pm.makePersistent(comment);
	    } finally {
	        pm.close();
	    }

	}

	@Override
	public boolean deleteComment(Comment comment) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Comment.class);
			query.setFilter("key == commentKeyParam");
			query.declareParameters("String commentKeyParam");
			
			@SuppressWarnings("unchecked")
			List<Comment> results = (List<Comment>) query.execute(comment.getKey());
			
			if(results.size() > 0) {
				query.deletePersistentAll(comment.getKey());
				return true;
			}
			
			return false;
			
		} finally {
	        pm.close();
	    }
		
	}

	@Override
	public List<Comment> getAllComments(String uploadName) {
		
		pm = PMF.get().getPersistenceManager();
		
		try {

			Query query = pm.newQuery(Comment.class);
			query.setFilter("uploadName == uploadNameParam");
			query.declareParameters("String uploadNameParam");
			
			@SuppressWarnings("unchecked")
			List<Comment> results = (List<Comment>) query.execute(uploadName);

			List<Comment> returnList = new ArrayList<Comment>();
			
			for(Comment currentComment : results) {
				returnList.add(currentComment);
			}
			
			return returnList;
			
		} finally {
	        pm.close();
	    }
		
	}

	
	
}
