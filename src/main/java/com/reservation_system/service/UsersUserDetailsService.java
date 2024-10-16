package com.reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reservation_system.entity.User;
import com.reservation_system.entity.model.UserModel;
import com.reservation_system.repository.UserRepository;

@Service
public class UsersUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user=userRepository.findByEmail(email);
		
		if(user==null)
			throw new UsernameNotFoundException("User not found here");
		
		return new UserModel(user);
  }

}
