package com.reservation_system.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String phone;
	
	private String Location;
	
	@OneToMany(mappedBy = "userToken")
	private List<Token> token;
	
}
