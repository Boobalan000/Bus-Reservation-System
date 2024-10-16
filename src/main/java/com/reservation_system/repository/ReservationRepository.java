package com.reservation_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservation_system.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByUserEmail(String email);

}
