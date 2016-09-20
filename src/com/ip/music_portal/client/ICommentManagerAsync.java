package com.ip.music_portal.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICommentManagerAsync {
	
	void addComment(Comment comment, AsyncCallback<Void> callback);

	void deleteComment(Comment comment, AsyncCallback<Boolean> callback);
	
	void getAllComments(String uploadName, AsyncCallback<List<Comment>> callback);
	
}
