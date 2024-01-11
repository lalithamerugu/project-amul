package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CattleDetails;
import com.example.demo.model.CattleRecords;
import com.example.demo.service.CattleService;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class CattleController {

	@Autowired
	private CattleService service;
//adding cattles to te list
	@PostMapping("/add/cattles")
	private ResponseEntity<String> addCattles(@RequestBody CattleDetails cattleDetails){
		ResponseEntity<String> response=null;
		try {
			response = new ResponseEntity<>(service.addCattle(cattleDetails), HttpStatus.OK); 
		} catch (RuntimeException e) {
			e.printStackTrace();
			response=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
//getting list of cattles
	@GetMapping("/get/cattles")
	private ResponseEntity<?> getCattles(){
		ResponseEntity<?> response=null;
		try {
			response=new ResponseEntity<>(service.getList(), HttpStatus.OK);
		}  catch (NullPointerException e) {
			e.printStackTrace();
			response=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
//updating the weight
	@PatchMapping("/update/weight/{name}/{weight}")
	private ResponseEntity<String> updateWeight(@PathVariable String name, @PathVariable Double weight){
		ResponseEntity<String> response=null;
		try {
			response = new ResponseEntity<>(service.updateWeight(name, weight), HttpStatus.OK); 
		} catch (NullPointerException e) {
			e.printStackTrace();
			response=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
//updating health records
	@PostMapping("/maintain/health/records")
	private ResponseEntity<String> updateHealthRecord(@RequestBody CattleRecords cattleRecords){
		ResponseEntity<String> response=null;
		try {
			response = new ResponseEntity<>(service.updatingHealthRecords(cattleRecords), HttpStatus.OK); 
		} catch (NullPointerException e) {
			e.printStackTrace();
			response=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
//getting health records
	@GetMapping("/get/health/records")
	private ResponseEntity<?> getHealthRecord(){
		ResponseEntity<?> response=null;
		try {
			response=new ResponseEntity<>(service.getHealthRecords(), HttpStatus.OK);
		}  catch (NullPointerException e) {
			e.printStackTrace();
			response=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
//deleting cattle
	@DeleteMapping("/delete/cattle/{name}")
	private ResponseEntity<String> deleteCattle(@PathVariable String name){
		ResponseEntity<String> response=null;
		try {
			response=new ResponseEntity<>(service.deleteCattle(name), HttpStatus.OK);
		}  catch (NullPointerException e) {
			e.printStackTrace();
			response=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			response=new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
}
