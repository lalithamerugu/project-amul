package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "season_tokens")
public class SeasonToken {
	@Id
	@GeneratedValue
	private Integer id;
	private String generatedOn;
	private String tokenId;
	private String isValid;
	private String belongsTo;
}
