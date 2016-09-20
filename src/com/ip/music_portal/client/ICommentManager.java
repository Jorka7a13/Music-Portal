package com.ip.music_portal.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("commentManager")
public interface ICommentManager extends RemoteService {
	
	void addComment(Comment comment);
	
	boolean deleteComment(Comment comment);
	
	List<Comment> getAllComments(String uploadName);
	
}
