package com.reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservation_system.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

}
