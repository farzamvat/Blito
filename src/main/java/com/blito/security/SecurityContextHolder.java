package com.blito.security;

import com.blito.models.User;

public class SecurityContextHolder {
	private final static ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();
	
	public static void setCurrentUser(User user)
	{
		THREAD_LOCAL.set(user);
	}
	
	public static User currentUser()
	{
		return THREAD_LOCAL.get();
	}
	public static void removeCurrentUser()
	{
		THREAD_LOCAL.remove();
	}
}
