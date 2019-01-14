package com.mysbpage.web;

import javax.servlet.http.HttpSession;

import com.mysbpage.domain.User;

public class HttpSessionUtils {
	public static final String USER_SESSION_KEY = "sessionUser";
	
	// 로그인 상태를 확인하기 위한 메소
	public static boolean isLoginUser(HttpSession session) {
		Object sessionedUser = session.getAttribute(USER_SESSION_KEY);
		if(sessionedUser == null) {
			return false;
		}
		return true;
	}
	
	// 로그인 상태일 때 유저 정보가 담긴 파라미터(getAttribute(USER_SESSION_KEY)를 반환)
	public static User getUserFromSession(HttpSession session) {
		if( !isLoginUser(session) ) {
			return null;
		}
		
		return (User)session.getAttribute(USER_SESSION_KEY);
	}
}
