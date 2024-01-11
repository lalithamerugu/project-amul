package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SeasonCustomer;


public interface SeasonCustomerRepo extends JpaRepository<SeasonCustomer, Integer>{
	public SeasonCustomer findByUniqueCustId(String uniqueCustId);
	Optional<SeasonCustomer> findByCustomerEmail(String email);
	public List<SeasonCustomer> findAllByQuantityAllowed(double quantity);
	public SeasonCustomer deleteByUniqueCustId(String uniqueCustId);
}
