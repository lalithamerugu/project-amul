package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AllTransactions;

public interface TransactionRepo extends JpaRepository<AllTransactions, Integer> {
	//List<AllTransactions> findAllByDate(String Date);
	List<AllTransactions> findAllByDateBetween(LocalDate fromDate, LocalDate toDate);
	AllTransactions findByTransactionId(String transactionId);
	List<AllTransactions> findAllByDate(LocalDate date);
	List<AllTransactions> findAllByCustomerId(String customerId);
}
