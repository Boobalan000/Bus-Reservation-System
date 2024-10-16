package com.reservation_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reservation_system.entity.Bus;

public interface BusRepository extends JpaRepository<Bus, Integer> {

	@Query("select b from Bus b where b.fromLocation like :from and b.toLocation like :to and b.date like :date")
	List<Bus> findByLocation(String from, String to, String date);

	List<Bus> findByBusNumberIn(List<String> bus);
}
