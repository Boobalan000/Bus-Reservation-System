package com.reservation_system.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.reservation_system.repository.TokenRepository;
import com.reservation_system.service.CustomUserDetailsService;
import com.reservation_system.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Cookie cookie=WebUtils.getCookie(request,"JWT");
		
		if(cookie!=null)
		{
			String authValue=cookie.getValue();
			String token=null;
			String email=null;
			
			if(authValue!=null&&authValue.startsWith("Bearer"))
			{
				token=authValue.substring(6);
				email=jwtService.extractEmail(token);
				
				if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null)
				{
					UserDetails userDetails=context.getBean(CustomUserDetailsService.class).loadUserByUsername(email);
					
					var isTokenValid=tokenRepository.findByToken(token)
							.map(t->!t.isExpired() && !t.isRevoked())
							.orElse(null);
					
					if(jwtService.validateToken(token, userDetails) && isTokenValid)
					{
						UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
