package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name="season_customer")
public class SeasonCustomer {
	@Id
	@GeneratedValue
	private Integer id;
	private String customerName;
	private String customerEmail;
	private String address;
	private String mobileNumber;
	private String lastVisitedTime;
	private double purchasedQuantity;
	private double creditAmount;
	private double paidAmount;
	private String uniqueCustId;
	@Column(name="quanity_allowed")
	private Double quantityAllowed;
	private String isActive;
}
