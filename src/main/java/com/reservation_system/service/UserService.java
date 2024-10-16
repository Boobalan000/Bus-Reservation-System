package com.reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.reservation_system.entity.Token;
import com.reservation_system.entity.User;
import com.reservation_system.entity.enums.TokenType;
import com.reservation_system.repository.TokenRepository;
import com.reservation_system.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private JwtService jwtService;
	
	private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
	
	public boolean findByUserEmail(String email)
	{
		return userRepository.findByEmail(email)!=null;
	}
	
	public boolean save(User user)
	{
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user)!=null;
	}
	
	public boolean verify(User user,HttpServletResponse response)
	{
		boolean result=false;
		
		try
		{
			Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
			
			if(authentication.isAuthenticated())
			{
				String token=jwtService.getToken(user.getEmail());
				User userFind=userRepository.findByEmail(user.getEmail());
				saveToken(token,userFind);
				generateCookies(token,response);
				result=true;
			}
		}
		catch(BadCredentialsException e)
		{
			System.out.println("User not found");
		}
		
		return result;
	}
	
	private void saveToken(String token,User user)
	{
		var tokenSave=Token.builder()
				.userToken(user)
				.token(token)
				.tokenType(TokenType.Bearer)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepository.save(tokenSave);
	}
	
	private void generateCookies(String token,HttpServletResponse response)
	{
		Cookie cookie=new Cookie("JWT","Bearer"+token);
		cookie.setHttpOnly(false);
		cookie.setSecure(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public User findByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}

	public User getUserById(int id) {
		return userRepository.findById(id).get();
	}

	public void saveUpdates(User users) {
		userRepository.save(users);		
	}
}
