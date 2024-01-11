package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SeasonToken;

public interface TokenInfoRepo extends JpaRepository<SeasonToken, Integer>{
	public SeasonToken findByTokenId(String tokenId);
}
