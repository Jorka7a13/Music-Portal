package com.ip.music_portal.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUploadManagerAsync {
	
	void addUpload(Upload upload, AsyncCallback<Void> callback);

	void deleteUpload(String uploadName, AsyncCallback<Boolean> callback);
	
	void containsUploadWithName(String uploadName, AsyncCallback<Boolean> callback);

	void uploadCount(AsyncCallback<Integer> callback);
	
	void getUploadByName(String uploadName, AsyncCallback<Upload> callback);
	
	void getAllUploadNames(AsyncCallback<List<String>> callback);
	
}
