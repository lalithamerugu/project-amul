package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class AdminDetails {
	@Id
	@GeneratedValue
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	
	//@JsonIgnore
	private String password;
}
