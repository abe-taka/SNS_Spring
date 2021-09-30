package com.example.demo.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Session_manage {

	public String Get_SessionMail(String mailaddress) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mailaddress = auth.getName();
		return mailaddress;
	}
}