package com.reservation_system.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.reservation_system.filter.JwtFilter;
import com.reservation_system.service.CustomUserDetailsService;
import com.reservation_system.service.LogoutService;



@EnableWebSecurity
@Configuration
public class SecurityConfig {

	 @Autowired 
	  private JwtFilter jwtFilter;

	  @Autowired
	  private CustomUserDetailsService userDetailsService;
	  
	  @Autowired
	  private LogoutService logoutHandler;
	
	@Bean
	 SecurityFilterChain security(HttpSecurity http) throws Exception
	{

       http.csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(request -> request.requestMatchers("/user/register", "/", "/user/login","/swagger-ui/index.html", "/v3/api-docs/**",      // OpenAPI documentation
                       "/swagger-ui/**",       // Swagger UI
                       "/swagger-resources/**", // Swagger resources
                       "/webjars/**").permitAll()
                       .requestMatchers("/user/**").hasRole("User").anyRequest().authenticated())
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               //http.httpBasic(Customizer.withDefaults());
               //http.formLogin(Customizer.withDefaults());
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
               .logout(logout -> logout
                       .logoutUrl("/user/logout")
                       .logoutSuccessUrl("/")
                       .addLogoutHandler(logoutHandler)
                       .logoutSuccessHandler((request, response, authentication) -> 
                       {
                       	SecurityContextHolder.clearContext();
                       	response.sendRedirect("/");
                       }));
		return http.build();
	}
	
	
	  @Bean 
	  AuthenticationProvider provide()
	  {
		  DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		  provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		  provider.setUserDetailsService(userDetailsService); 
		  return provider; 
	  }
	  
	  @Bean
	  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	  {
		  return config.getAuthenticationManager();
	  }
	 
}
