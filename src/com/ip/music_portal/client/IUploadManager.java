package com.ip.music_portal.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("uploadManager")
public interface IUploadManager extends RemoteService {

	void addUpload(Upload upload);
	
	boolean deleteUpload(String uploadName);
	
	boolean containsUploadWithName(String uploadName);

	int uploadCount();
	
	Upload getUploadByName(String uploadName);
	
	List<String> getAllUploadNames();
	
}
