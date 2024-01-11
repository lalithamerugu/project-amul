package com.example.demo.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class EarningsAndPendings {

	@Id
	@GeneratedValue
	private Integer id;
	private LocalDate date;
	private Double receivedAmount;
	private Double SpendAmount;
	private Double pendingAmount;
}
