package com.example.demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CattleDetails;
import com.example.demo.model.CattleRecords;
import com.example.demo.repository.CattleDetailsRepo;
import com.example.demo.repository.CattleRecordsRepo;

@Service
public class CattleService {

	@Autowired
	private CattleDetailsRepo cattleRepo;
	
	@Autowired
	private CattleRecordsRepo cattleRecordRepo;
	
//date conversion
	LocalDate localDate=LocalDate.now();
	DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
	String formattedDate=localDate.format(formatter);
	LocalDate parsedDate=LocalDate.parse(formattedDate, formatter);
//adding cattles to todays list
	CustomerService cs=new CustomerService();
	public String addCattle(CattleDetails cattleDetails) {
		CattleDetails cattle=this.cattleRepo.findByName(cattleDetails.getName());
		if(cattle==null) {
			String uniqueId="CTL"+cs.generateRandomID(5);
			cattleDetails.setLastUpdatedAt(parsedDate);
			cattleDetails.setUniqueId(uniqueId);
			this.cattleRepo.save(cattleDetails);
			return "Added successfully";
		} else {
			throw new RuntimeException("Duplicate name entered");
		}
	}
//getting cattles list
	public List<Map<String, Object>> getList(){
		
		List<Map<String, Object>> l=new ArrayList<>();
		LocalDate currentDate=LocalDate.now();
		
		List<CattleDetails> list=this.cattleRepo.findAll();
		if(list.isEmpty())
			throw new NullPointerException("Empty list");
		else {
			for(CattleDetails cd:list) {
				Map<String, Object> map=new LinkedHashMap<>();
				map.put("Cattle id", cd.getUniqueId());
				map.put("Name", cd.getName());
				map.put("Animal", cd.getAnimal());
				map.put("Breed", cd.getBreed());
				map.put("Born On", cd.getDateOfBirth());
				map.put("Birth weight", cd.getWeightAtTimeOfBirth());
				map.put("Weight", cd.getCurrentWeight());
				map.put("Details updated on", cd.getLastUpdatedAt());
				LocalDate birthLocalDate=LocalDate.parse(cd.getDateOfBirth());
				int age=Period.between(birthLocalDate, currentDate).getYears();
				map.put("Age in years", age);
				
				l.add(map);
			}
		}
		return l;
	}
//updating cattle weigght
	public String updateWeight(String name, Double weight) {
		CattleDetails cattle=this.cattleRepo.findByName(name);
		CattleRecords record=this.cattleRecordRepo.findByDateAndName(parsedDate, name);
		if(record!=null) {
			record.setWeight(weight);
			this.cattleRecordRepo.save(record);
		}else if(cattle==null)
			throw new NullPointerException("no cattle with name '"+name+"' found");
		else {
			cattle.setCurrentWeight(weight);
			cattle.setLastUpdatedAt(parsedDate);
			this.cattleRepo.save(cattle);
		}
		return "weight updated successfully";
	}
//deleting cattle
	public String deleteCattle(String name) {
		CattleDetails cattle=this.cattleRepo.findByName(name);
		if(cattle==null)
			throw new NullPointerException("No cattle with name '"+name+"' found");
		else {
			this.cattleRepo.delete(cattle);
			return "Deleted Successfully";
		}
	}
//maintining health records
	public String updatingHealthRecords(CattleRecords record) {
		CattleDetails details=this.cattleRepo.findByName(record.getName());
		CattleRecords record1=this.cattleRecordRepo.findByDateAndName(parsedDate, record.getName());
		if(details==null)
			throw new NullPointerException("No cattle found with name '"+record.getName()+"'");
		else if(record1==null) {
			record.setDate(parsedDate);
			if(record.getVisitedVet()==null)
				record.setVisitedVet("NO");
			this.cattleRecordRepo.save(record);
			return "Inserted Successfully";
		} else {
			record1.setDate(parsedDate);
			record1.setMilkGave(record.getMilkGave());
			if(record.getWeight()!=null)
				record1.setWeight(record.getWeight());
			return "Updated Successfully";
		}
	}
//getting health records
	public List<CattleRecords> getHealthRecords(){
		List<CattleRecords> list=this.cattleRecordRepo.findAll();
		if(list.isEmpty())
			throw new NullPointerException("Record is empty");
		else
			return list;
	}
}
