package com.reservation_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservation_system.entity.Bus;
import com.reservation_system.repository.BusRepository;

@Service
public class BusService {

	@Autowired
	private BusRepository busRepository;
	
	public List<Bus> getBuses(String from,String to,String date )
	{
		return busRepository.findByLocation(from, to, date);
	}

	public Bus findById(int id) {
		return busRepository.findById(id).get();
	}

	public List<Bus> getBusesByBusNumber(List<String> bus) {
		return busRepository.findByBusNumberIn(bus);
	}
}
