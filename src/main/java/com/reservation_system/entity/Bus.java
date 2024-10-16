package com.reservation_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bus {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String busNumber;
	
	private String busName;

	private String fromLocation;
	
	private String toLocation;
	
	private String date;
	
	private String departure;
	
	private String arrival;
	
	private String duration;
	
	private int price;
	
	private int seats;
	

	
}
