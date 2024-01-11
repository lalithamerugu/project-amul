package com.example.demo.utils;

import lombok.Data;

@Data
public class DashboardDataResponse {
	private Double receivedAmount;
	private Double availableQuantity;
	private Double cashInTheCounter;
	private Double costPerLitre;
	private Double yetToReceive;
	private String lastUpdatedAt;
}
