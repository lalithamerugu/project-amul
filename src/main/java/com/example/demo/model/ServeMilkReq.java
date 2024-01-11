package com.example.demo.model;

import lombok.Data;

@Data
public class ServeMilkReq {
	private Integer id;
	private String token;
	private String paymentMethod;
}
