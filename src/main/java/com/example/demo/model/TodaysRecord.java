package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data

public class TodaysRecord {
	@Id
	private Integer id=1;
	@Transient
	private String customerId;
	@Transient
	private Double receivedAmount;
	private Double availableQuantity;
	private Double cashInTheCounter;
	private Double costPerLitre;
	private Double yetToReceive;
	private String lastUpdatedAt;
}
