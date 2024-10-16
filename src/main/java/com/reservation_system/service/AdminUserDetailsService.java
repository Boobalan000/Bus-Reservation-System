package com.reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reservation_system.entity.Admin;
import com.reservation_system.entity.model.AdminModel;
import com.reservation_system.repository.AdminRepository;

@Service
public class AdminUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Admin admin=adminRepository.findByEmail(email);
		
		if(admin==null)
			throw new UsernameNotFoundException("Admin not found");
		
		return new AdminModel(admin);
	}

}
