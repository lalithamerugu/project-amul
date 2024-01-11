package com.example.demo.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class CattleRecords {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private Double milkGave;
	private Double weight;
	private LocalDate date;
	private String visitedVet;
}
