package com.example.demo.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.EarningsAndPendings;

public interface EarningsAndPendingsRepo extends JpaRepository<EarningsAndPendings, Integer> {
	EarningsAndPendings findByDate(LocalDate date);
}
