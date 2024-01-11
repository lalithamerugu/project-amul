package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CattleDetails;

public interface CattleDetailsRepo extends JpaRepository<CattleDetails, Integer> {
	public CattleDetails findByName(String name);
	public String deleteByIgnoreCaseName(String name);
}
