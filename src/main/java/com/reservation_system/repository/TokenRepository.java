package com.reservation_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reservation_system.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {


	@Query("""
			select t from Token t
			join t.userToken u
			where u.id= :userId and (t.expired=false or t.revoked=false)
			""")
	List<Token> findAllValidTokenByUser(@Param("userId") Integer userId);
	
	@Query("""
			select t from Token t
			join t.adminToken d
			where d.id= :doctorId and (t.expired=false or t.revoked=false)
			""")
	List<Token> findAllValidTokenByAdmin(@Param("doctorId") Integer doctorId);
	
	Optional<Token> findByToken(String token);
}
