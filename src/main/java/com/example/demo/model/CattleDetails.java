package com.example.demo.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class CattleDetails {

	@Id
	@GeneratedValue
	private Integer id;
	private String uniqueId;
	private String animal;
	private String name;
	private String breed;
	private String dateOfBirth;
	private Double weightAtTimeOfBirth;
	private Double currentWeight;
	private LocalDate lastUpdatedAt;
}
