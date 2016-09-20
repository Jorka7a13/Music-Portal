package com.ip.music_portal.shared;

public class LoginVerifier {

	public static boolean isUserNameValid(String userName) {
		if(userName == null) 
			return false;
		
		return userName.length() >= 6;
	}
	
	public static boolean isPasswordValid(String password) {
		if(password == null) 
			return false;

		return password.length() >= 4;
	}
	
	public static boolean isEmailValid(String email) {
		if(email == null) 
			return false;
		
		if(!email.contains("@"))  
			return false;
		
		if(!email.contains("."))  
			return false;
		
		if(email.length() > 6) 
			return true;
		
		return true;
	}
	
}
