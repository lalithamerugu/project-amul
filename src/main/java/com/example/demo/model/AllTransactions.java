package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class AllTransactions {

	@Id
	@GeneratedValue
	private Integer id;
	private String transactionId;
	private LocalDate date;
	private LocalDateTime time;
	private String description;
	private Double paidAmount;
	private Double receivedAmount;
	private String customerId;
	
}
