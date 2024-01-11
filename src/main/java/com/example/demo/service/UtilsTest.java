package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilsTest {
	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		System.out.println(today);
		LocalDateTime time = LocalDateTime.now();
		System.out.println(time);
		
		 LocalDate localDate = LocalDate.now();

	        // Define a custom pattern
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	        // Format the LocalDate using the custom pattern
	        String formattedDate = localDate.format(formatter);
	        System.out.println("Formatted Date: " + formattedDate);

	        // Parse a string into a LocalDate using the custom pattern
	        String dateString = "31-12-2022";
	        LocalDate parsedDate = LocalDate.parse(dateString, formatter);
	        System.out.println("Parsed Date: " + parsedDate);
	}
}
