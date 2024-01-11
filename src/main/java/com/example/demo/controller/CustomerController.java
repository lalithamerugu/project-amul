package com.example.demo.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AllTransactions;
import com.example.demo.model.SeasonCustomer;
import com.example.demo.model.ServeMilkReq;
import com.example.demo.model.TodaysRecord;
import com.example.demo.service.CustomerService;
import com.example.demo.service.MilkServiceException;
import com.example.demo.utils.DashboardDataResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	Double totalAmount=0.0;
//saving customers
	@PostMapping("/save/customers/info")
	private ResponseEntity<String> saveSeasonCustomer(@RequestBody SeasonCustomer seasonCustmer){
		ResponseEntity<String> resp = null;
		try {
			String response = this.service.saveSeasonCustomerInfo(seasonCustmer);
			resp = new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch(IllegalArgumentException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(RuntimeException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
	
//getting todays record
	@GetMapping("/todays/record")
	public ResponseEntity<?> fetchRecord() {
		ResponseEntity<?> response = null;
		try {
			TodaysRecord record=service.getRecord();
			if(record==null) {
				response = new ResponseEntity<>("Set Record First", HttpStatus.OK);
			} else {
				Map<String, Object> t=new HashMap<>();
				t.put("availableQty: ",record.getAvailableQuantity());
				t.put("counterCash : ",record.getCashInTheCounter());
				t.put("costPerLiter : ",record.getCostPerLitre());
				if(record.getYetToReceive()==null)
					record.setYetToReceive(0.0);
				t.put("pendingAmount : ",record.getYetToReceive());
				response = new ResponseEntity<>(t, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
@GetMapping("/fetch/dashboard/data")
public ResponseEntity<DashboardDataResponse> fetchDashboardData() {
	TodaysRecord record=service.getRecord();
	
	DashboardDataResponse dashboardResponse = new DashboardDataResponse();
	dashboardResponse.setAvailableQuantity(record.getAvailableQuantity());
	dashboardResponse.setCashInTheCounter(record.getCashInTheCounter());
	dashboardResponse.setCostPerLitre(record.getCostPerLitre());
	dashboardResponse.setReceivedAmount(record.getReceivedAmount());
	dashboardResponse.setYetToReceive(record.getYetToReceive());
	//Fetching the customers count 
	
	
	return ResponseEntity.ok(dashboardResponse);
}
//getting all customers
	@GetMapping("/fetch/customers")
	public ResponseEntity<?> fetchAllCustomers(@RequestParam(value="pageNumber", defaultValue="0", required=false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue="5", required=false) Integer pageSize){
		ResponseEntity<?> response=null;
		try {
			response = new ResponseEntity<>(this.service.fetchAllCustomers(pageNumber ,pageSize), HttpStatus.OK);
		} catch(NullPointerException e) {
			response = new ResponseEntity<>("Nothing to display", HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch (Exception e) {
			response = new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}
	
//fetching all transactions
	@GetMapping("/all/transactions")
	public ResponseEntity<?> fetchAllTransactions(@RequestParam(value="pageNumber", defaultValue="0", required=false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue="3", required=false) Integer pageSize){
		
		ResponseEntity<?> response=null;
		try {
			response = new ResponseEntity<>(this.service.fetchAllTransactions(pageNumber ,pageSize), HttpStatus.OK);
		} catch(NullPointerException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch (Exception e) {
			response = new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}
//generating tokens
	@PostMapping("/generate/tokens")
	private ResponseEntity<String> generateTokensForToday(){
		ResponseEntity<String> response=null;
		try {
			response = new ResponseEntity<>(this.service.generateTokensForCustomersToday(), HttpStatus.CREATED);
		} catch (NullPointerException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			response = new ResponseEntity<>("Something Went Wrong", HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		return response;
	}
//getting token info by their id
	@GetMapping("/get/token/info/{tokenId}")
	public ResponseEntity<?> getTokenInfo(@PathVariable String tokenId){
		ResponseEntity<?> response=null;
		try {
			response = new ResponseEntity<>(this.service.getTokenInfoByTokenId(tokenId), HttpStatus.OK);
		} catch(NullPointerException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(Exception e) {
			response = new ResponseEntity<>("Something Went Wrong", HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		return response;
	}
//serving milk
	@PostMapping("/serve/milk")
	public ResponseEntity<String> serveMilk(@RequestBody ServeMilkReq milkReq){
		ResponseEntity<String> response=null;
		try {
			response = new ResponseEntity<>(this.service.serveMilk(milkReq), HttpStatus.OK);
		} catch (MilkServiceException e) {
	        response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	        e.printStackTrace();
	    } catch(NullPointerException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(RuntimeException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(Exception e) {
			response = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}
//serving milk for guest customers
	@PostMapping("/serve/milk/guest/{quantity}")
	public ResponseEntity<String> serveMilkGuest(@PathVariable Double quantity){
		ResponseEntity<String> response=null;
		try {
			response = new ResponseEntity<>(this.service.serveMilkForGuest(quantity), HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(Exception e) {
			response = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}
//getting single customer by their unique customer id
	@GetMapping("/get/single/customer/{customerId}")
	public ResponseEntity<?> getCustomerById(@PathVariable String customerId){
		ResponseEntity<?> resp=null;
		try {
			SeasonCustomer response=service.getCustomerInfoByUniqueId(customerId);
			resp = new ResponseEntity<>(response, HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
//getting active tokens
	@GetMapping("/get/active/tokens")
	public ResponseEntity<?> getActiveTokens(){
		ResponseEntity<?> resp=null;
		try {
			resp = new ResponseEntity<>(service.fetchActiveTokens(), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
	@GetMapping("/get/all/tokens")
	public ResponseEntity<?> getAllTokens(){
		ResponseEntity<?> resp=null;
		try {
			resp = new ResponseEntity<>(service.fetchAllTokens(), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
//getting customers who are receiving same quantity
	@GetMapping("/get/customers/by/quantity/{quantity}")
	public ResponseEntity<?> getCustomersByQuantity(@PathVariable double quantity){
		ResponseEntity<?> resp=null;
		try {
			resp = new ResponseEntity<>(service.fetchByQuantity(quantity), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
//egtting customers whose amount is pendng
	@GetMapping("/get/customers/with/pendingamount")
	public ResponseEntity<?> getCustomersByPendingAmount(){
		ResponseEntity<?> resp=null;
		try {
			resp = new ResponseEntity<>(service.fetchPendingCustomers(), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
//Collecting credit amount
	@PutMapping("/collect/credit/amount")
	public ResponseEntity<String> collectCredit(@RequestBody TodaysRecord record){
		ResponseEntity<String> resp=null;
		try {
			resp = new ResponseEntity<>(service.collectAmount(record), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
//paying bills	
	@PostMapping("/paying/bills/{paidTo}")
	public ResponseEntity<String> payBill(@RequestBody AllTransactions allTransactions, @PathVariable String paidTo, @RequestParam(value="name", required=false) String name){
		ResponseEntity<String> resp=null;
		try {
			resp = new ResponseEntity<>(service.raiseExpense(allTransactions, paidTo, name), HttpStatus.CREATED);
		} catch(RuntimeException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
	
	@PostMapping("/expense/record")
	public ResponseEntity<String> expense(@RequestBody AllTransactions allTransactions){
		System.out.println("Executing expense endpoint");
		ResponseEntity<String> resp=null;
		try {
			resp = new ResponseEntity<>(service.raiseExpenseBill(allTransactions), HttpStatus.CREATED);
		} catch(RuntimeException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
//retrieving transactions between specific days
	@GetMapping("/retrieve/transactions/{fromDate}/{toDate}")
	private ResponseEntity<?> retrieveTransactions(@PathVariable String fromDate, @PathVariable String toDate){
		ResponseEntity<?> resp=null;
		try {
			 String dateString1 = fromDate;
			// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 LocalDate localDate1 = LocalDate.parse(dateString1);
		     
		     String dateString2 = toDate;
		     LocalDate localDate2 = LocalDate.parse(dateString2);

			resp = new ResponseEntity<>(service.fetchTransactions(localDate1, localDate2), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
//calculating income on given date
	@GetMapping("/cash/collected/on/{date}")
	public ResponseEntity<String> cashCalculate(@PathVariable String date) {
		ResponseEntity<String> resp=null;
		try {
			String date1=date;
			LocalDate localDate= LocalDate.parse(date1);
			resp= new ResponseEntity<String>(service.getAmountByDates(localDate), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp= new ResponseEntity<String>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
//getting everyday earnings and pending amounts
	@GetMapping("/cash/collected/on/everyday")
	public ResponseEntity<?> getEarnings() {
		ResponseEntity<?> resp=null;
		try {
			resp= new ResponseEntity<>(service.getEarnings(), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp= new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp= new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
	@GetMapping("/getting/purchase/history/{id}")
	public ResponseEntity<?> gettingPurchaseHistory(@PathVariable String id) {
		ResponseEntity<?> resp=null;
		try {
			resp= new ResponseEntity<>(service.getCustomerPurchaseHistoryByUniqueId(id), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp= new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp= new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
	@GetMapping("/fetch/purchase/history/{id}")
	public ResponseEntity<List<AllTransactions>> fetchCustomerTransactionsHistory(@PathVariable String id) {
		List<AllTransactions> list = service.fetchCustomerTransactions(id);
		return ResponseEntity.ok(list);
	}
	//fetchCustomerTransactions
	@PutMapping("/set/available/quantity")
	public ResponseEntity<String> setAvailableQuantity(@RequestBody TodaysRecord record){
		System.out.println("Executing Update Available QTY Endpoint");
		System.out.println(record);
		ResponseEntity<String> resp=null;
		try {
			resp = new ResponseEntity<>(service.updatingAvailableQuantity(record), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(RuntimeException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	} 
	
	//updating active status for customer
	@PutMapping("/active/status")
	public ResponseEntity<String> setActiveStatus(@RequestBody SeasonCustomer customer){
		ResponseEntity<String> resp=null;
		try {
			resp = new ResponseEntity<>(service.setIsActive(customer), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			e.printStackTrace();
		} catch(Exception e) {
			resp = new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	} 
	
	@DeleteMapping("/dodelete/{customerId}")
	private ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
		ResponseEntity<String> resp=null;
		try {
			resp= new ResponseEntity<String>(service.deleteCustomer(customerId), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(IllegalArgumentException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp= new ResponseEntity<String>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
	
	@DeleteMapping("/delete/expense/{transactionId}")
	private ResponseEntity<String> deleteTransaction(@PathVariable String transactionId) {
		ResponseEntity<String> resp=null;
		try {
			resp= new ResponseEntity<String>(service.deleteExpense(transactionId), HttpStatus.OK);
		} catch(NullPointerException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(IllegalArgumentException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp= new ResponseEntity<String>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
	
	@PatchMapping("/update/customer")
	public ResponseEntity<String> updateCustomer(@RequestBody SeasonCustomer customer) {
		ResponseEntity<String> resp=null;
		try {
			resp= new ResponseEntity<String>(service.updateCustomer(customer), HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(NullPointerException e) {
			resp= new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			resp= new ResponseEntity<String>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resp;
	}
}
