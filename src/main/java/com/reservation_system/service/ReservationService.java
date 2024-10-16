package com.reservation_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservation_system.entity.Reservation;
import com.reservation_system.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;
	
	public boolean save(Reservation reservation)
	{
		return reservationRepository.save(reservation)!=null;
	}

	public List<Reservation> getByUserEmail(String email) {
		return reservationRepository.findByUserEmail(email);
	}
}
