package com.example.demo.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CattleRecords;

public interface CattleRecordsRepo extends JpaRepository<CattleRecords, Integer> {
	CattleRecords findByDateAndName(LocalDate date, String name);
}
