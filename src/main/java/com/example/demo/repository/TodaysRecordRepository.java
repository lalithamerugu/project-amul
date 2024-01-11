package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TodaysRecord;

public interface TodaysRecordRepository extends JpaRepository<TodaysRecord, Integer> {
	
}
