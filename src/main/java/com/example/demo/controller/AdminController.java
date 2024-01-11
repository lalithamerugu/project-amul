package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AdminDetails;
import com.example.demo.model.TodaysRecord;
import com.example.demo.service.CustomerService;

@RestController
@RequestMapping("/rest")
@CrossOrigin("*")
public class AdminController {
	
	@Autowired
	private CustomerService service;
	
	@PostMapping("/saveDetails")
	private ResponseEntity<String> saveData(@RequestBody AdminDetails ad) {
		ResponseEntity<String> resp=null;
		try {
			String s = service.findByEmail(ad);
			resp = new ResponseEntity<>(s, HttpStatus.CREATED);
		} catch (Exception e) {
			resp = new ResponseEntity<>("Email Already Exists",HttpStatus.CONFLICT);
			e.printStackTrace();
		}
		return resp;
	}
	
	@PostMapping("/login")
	private ResponseEntity<String> login(@RequestBody AdminDetails ad){
		System.out.println("Executing login Endpoint");
		ResponseEntity<String> resp=null;
		try {
			String s = service.findByEmailAndPassword(ad);
			resp = new ResponseEntity<>(s, HttpStatus.OK);
		} catch (Exception e) {
			resp = new ResponseEntity<>("Bad Credentials", HttpStatus.UNAUTHORIZED);
			e.printStackTrace();
		}
		return resp;
	}
	
	@PostMapping("/save/farm/summary")
	private ResponseEntity<String> saveFarmSummary(@RequestBody TodaysRecord dairyFarmSummary){
		ResponseEntity<String> resp=null;
		try {
			String response= service.saveDairyFarmSummary(dairyFarmSummary);
			resp=new ResponseEntity<>(response, HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			resp= new ResponseEntity<>("Invalid numbers entered", HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
	
}
