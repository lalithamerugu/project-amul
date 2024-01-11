package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AdminDetails;

public interface AdminRepository extends JpaRepository<AdminDetails, Integer> {
	Optional<AdminDetails> findByEmail(String email);
	Optional<AdminDetails> findByEmailAndPassword(String email, String password);
}
