package com.reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersUserDetailsService usersUserDetailsService;
	
	@Autowired
	private AdminUserDetailsService adminUserDetaisService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		if(email.endsWith("@admin.com"))
		{
			return adminUserDetaisService.loadUserByUsername(email);
		}
		return usersUserDetailsService.loadUserByUsername(email);
	}

}
