package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.AdminDetails;
import com.example.demo.model.AllTransactions;
import com.example.demo.model.CattleDetails;
import com.example.demo.model.SeasonCustomer;
import com.example.demo.model.SeasonToken;
import com.example.demo.model.ServeMilkReq;
import com.example.demo.model.TodaysRecord;
import com.example.demo.model.CattleRecords;
import com.example.demo.model.EarningsAndPendings;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CattleDetailsRepo;
import com.example.demo.repository.CattleRecordsRepo;
import com.example.demo.repository.EarningsAndPendingsRepo;
import com.example.demo.repository.SeasonCustomerRepo;
import com.example.demo.repository.TodaysRecordRepository;
import com.example.demo.repository.TokenInfoRepo;
import com.example.demo.repository.TransactionRepo;

@Service
public class CustomerService{

	@Autowired
	private AdminRepository repo;

	@Autowired
	private TodaysRecordRepository aqrepo;

	@Autowired
	private SeasonCustomerRepo srepo;
	
	@Autowired
	private TokenInfoRepo trepo;
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	@Autowired
	private CattleRecordsRepo cattleRecordsRepo;
	
	@Autowired
	private CattleDetailsRepo cattleDetailsRepo;
	
	@Autowired
	private EarningsAndPendingsRepo earningsAndPendingsRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//date conversion
	LocalDate localDate = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	String formattedDate = localDate.format(formatter);
	String dateString = formattedDate;
	LocalDate parsedDate = LocalDate.parse(dateString, formatter);
	
	public String findByEmail(AdminDetails ad) {
		String password = ad.getPassword();
		Optional<AdminDetails> admin = repo.findByEmail(ad.getEmail());
		if (admin.isPresent()) {
			throw new RuntimeException("Duplicate Email Entered");
		} else {
			//ad.setPassword(bCryptPasswordEncoder.encode(password));
			repo.save(ad);
			return "User Registered Successfully!!!";
		}
	}

	public String findByEmailAndPassword(AdminDetails ad) {
		//String hashedPassword = bCryptPasswordEncoder.encode(ad.getPassword());
		System.out.println(ad.getEmail());
		System.out.println(ad.getPassword());
		Optional<AdminDetails> adminDetails = repo.findByEmailAndPassword(ad.getEmail(), ad.getPassword());
		if (adminDetails.isPresent()) {
			return "Login Successful";
		} else {
			throw new RuntimeException("Bad Credentials");
		}
	}
	
//fetching all transactions
	public List<AllTransactions> fetchAllTransactions(Integer pageNumber, Integer pageSize) {
		Pageable p=PageRequest.of(pageNumber, pageSize);
		Page<AllTransactions> pagePost=transactionRepo.findAll(p);
		List<AllTransactions> list=pagePost.getContent();
		if(list.isEmpty())
			throw new NullPointerException("No Transactions Happened Till Now");
		else
			return list;
	}

//fetching all customers
	public List<SeasonCustomer> fetchAllCustomers(Integer pageNumber, Integer pageSize){
		Pageable p=PageRequest.of(pageNumber, pageSize);
		Page<SeasonCustomer> pagePost=this.srepo.findAll(p);
		List<SeasonCustomer> list = pagePost.getContent();
		if(list.size() == 0 || list == null) {
			throw new NullPointerException("No Customers found in the database");
		} else
			return list;
	}
//generating otp's and id's
	public static String generateRandomID(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}
	
	public String getCurrentTime() {
		LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = now.format(format);  
        return formatDateTime;
	}
//getting todays record
	public TodaysRecord getRecord() {
		Optional<TodaysRecord> dfs=aqrepo.findById(1);
		if(dfs.isPresent()) {
			TodaysRecord df1=dfs.get();
			return df1;
		}
		else
			return null;
	}
//fetching active tokens	
	public List<SeasonToken> fetchActiveTokens(){
		List<SeasonToken> list=new ArrayList<>();
		List<SeasonToken> tokens=trepo.findAll();
		if(tokens==null||tokens.size()==0) {
			throw new NullPointerException("No Tokens Found, Generate Tokens");
		} else {
			for(SeasonToken tokens1:tokens) {
				if((tokens1.getIsValid()).equalsIgnoreCase("yes"))
					list.add(tokens1);
			}
			return list;
		}
	}
	//fetching all tokens	
		public List<SeasonToken> fetchAllTokens(){
			List<SeasonToken> list=new ArrayList<>();
			List<SeasonToken> tokens=trepo.findAll();
			if(tokens==null||tokens.size()==0) {
				throw new NullPointerException("No Tokens Found, Generate Tokens");
			} else {
				
				
			}
			return tokens;
		}
//fetching customers by quantity
	public List<SeasonCustomer> fetchByQuantity(double quantity) {
		List<SeasonCustomer> list=srepo.findAllByQuantityAllowed(quantity);
		for(SeasonCustomer list1 : list) {
			System.out.println(list1);
		}
		if(list.size()==0) {
			throw new NullPointerException("Currently No Customers are receiving "+quantity+" litres of milk");
		} else {
			return list;
		}
	}
	//setting todays record
		public String saveDairyFarmSummary(TodaysRecord aq) {
			
			TodaysRecord dairyFarmSummary=this.getRecord();
			
			Double quantity = aq.getAvailableQuantity();
			Double costPerLitre = aq.getCostPerLitre();
			Double moneyInCounter=aq.getCashInTheCounter();
			
			if (quantity <= 0 || costPerLitre <= 0 || moneyInCounter < 0) {
				throw new IllegalArgumentException("user has entered invalid inputs");
			} else if(dairyFarmSummary!=null) {
				dairyFarmSummary.setAvailableQuantity(aq.getAvailableQuantity());
				dairyFarmSummary.setCashInTheCounter(moneyInCounter);
				dairyFarmSummary.setCostPerLitre(costPerLitre);
				dairyFarmSummary.setLastUpdatedAt(getCurrentTime());
				aqrepo.save(dairyFarmSummary);
				return "Available Quantity Is "+quantity;
				
			} else {
				aq.setLastUpdatedAt(getCurrentTime());
				aqrepo.save(aq);
				return "Available Quantity Is "+quantity;
			}
		}
//fetching customers whose amount is pending
	public List<SeasonCustomer> fetchPendingCustomers(){
		List<SeasonCustomer> customerList=new ArrayList<>();
		List<SeasonCustomer> list=this.fetchAllCustomers(0, 1000000000);
		if(list.size()==0) {
			throw new NullPointerException("No Customers Present at the moment");
		} else {
			for(SeasonCustomer list1:list) {
				if(list1.getCreditAmount()>0) {
					customerList.add(list1);
				}
			}
			return customerList;
		}
	}
//getting customer purchase hhistory by their id
	public Map<String, Object> getCustomerPurchaseHistoryByUniqueId(String uniqueCustomerId){
		Map<String, Object> map=new LinkedHashMap<>();
		SeasonCustomer customer=this.srepo.findByUniqueCustId(uniqueCustomerId);
		if(customer==null)
			throw new NullPointerException("No customer found with id "+uniqueCustomerId);
		else {
			map.put("Name", customer.getCustomerName());
			map.put("Active Status", customer.getIsActive());
			map.put("Last Visited On", customer.getLastVisitedTime());
			map.put("Quantity purchased till now", customer.getPurchasedQuantity());
			
			List<AllTransactions> list=this.transactionRepo.findAllByCustomerId(uniqueCustomerId);
			map.put("Purchase History", list);
			return map;
		}
	}
	
	public List<AllTransactions> fetchCustomerTransactions(String customerId){
		SeasonCustomer customer=this.srepo.findByUniqueCustId(customerId);
		if(customer==null)
			throw new NullPointerException("No customer found with id "+customerId);
		
			
			List<AllTransactions> list=this.transactionRepo.findAllByCustomerId(customerId);
			
			return list;
		
	}
//getting customer info by their id	
	public SeasonCustomer getCustomerInfoByUniqueId(String uniqueCustomerId) {
		SeasonCustomer customer = this.srepo.findByUniqueCustId(uniqueCustomerId);
		if(customer == null)
			throw new NullPointerException("No Customer Found With Id "+uniqueCustomerId);
		else {
			return customer;
		}
	}
//********************************************************************************************************************	
//saving season customer
	public String saveSeasonCustomerInfo(SeasonCustomer seasonCustmer) {
		
		Optional<SeasonCustomer> sc=srepo.findByCustomerEmail(seasonCustmer.getCustomerEmail());
		if(!sc.isPresent()) {
			if (seasonCustmer.getMobileNumber()==null||seasonCustmer.getCustomerEmail()==null||seasonCustmer.getCustomerName()==null||seasonCustmer.getQuantityAllowed()==null) {
				throw new NullPointerException("Fields Cannot be Empty");
			} else if((seasonCustmer.getMobileNumber()).length()!=10) {
				throw new IllegalArgumentException("Mobile number should consists of exactly 10 digits");
			} else if((seasonCustmer.getQuantityAllowed())<=0) {
				throw new IllegalArgumentException("Quantity is invalid");
			} else {
				String uniqueCustId = "2023"+generateRandomID(5);
				String customerName = seasonCustmer.getCustomerName();
				seasonCustmer.setPurchasedQuantity(0);
				seasonCustmer.setPaidAmount(0);
				seasonCustmer.setCreditAmount(0);
				seasonCustmer.setUniqueCustId(uniqueCustId);
				seasonCustmer.setIsActive("YES");
				String repsonse = "Generated Unique Customer Id for "+customerName +" is "+uniqueCustId;
				this.srepo.save(seasonCustmer);
				return repsonse;
			}
		} else {
			throw new RuntimeException("Customer Already Exists With Email id "+seasonCustmer.getCustomerEmail());
		}
	}
//setting active status
	public String setIsActive(SeasonCustomer seasonCustomer) {
		String id=seasonCustomer.getUniqueCustId();
		try {
			SeasonCustomer customer=this.getCustomerInfoByUniqueId(id);
			customer.setIsActive(seasonCustomer.getIsActive());
			srepo.save(customer);
			return "Customer with id : "+id+" active status is set to '"+seasonCustomer.getIsActive()+"'";
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}
	}
//generating tokens for active customers only
	public String generateTokensForCustomersToday() {
		try {
			List<SeasonCustomer> customers = fetchAllCustomers(0, 1000000000);
			for (SeasonCustomer customer : customers) {
				if((customer.getIsActive()).equalsIgnoreCase("YES")) {
					String uniqueToken = this.generateRandomID(5);
					String generatedToken = "AMUL" + uniqueToken;
					System.out.println("Generating Token " + customer.getCustomerName() + " : " + generatedToken);
					String currentTime = this.getCurrentTime();
					SeasonToken token = new SeasonToken();
					token.setBelongsTo(customer.getUniqueCustId());
					token.setGeneratedOn(currentTime);
					token.setTokenId(generatedToken);
					token.setIsValid("YES");
					this.trepo.save(token);	
				}
			}
			String response = "Tokens Generated Successfully on " + getCurrentTime();
			return response;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new NullPointerException("No Customers Found To Generate Tokens");
		}
	}
//getting token info by token id
	public SeasonToken getTokenInfoByTokenId(String tokenId) {
		SeasonToken token = this.trepo.findByTokenId(tokenId);
		System.out.println(token);
		if(token == null)
			throw new MilkServiceException("Invalid Token");
		else
			return token;
	}
//serving milk
	public String serveMilk(ServeMilkReq milkReq ) {
		System.out.println(milkReq);
		try {
			String transactionId="TNS"+generateRandomID(5);
			
			TodaysRecord tRecord=this.getRecord();
			SeasonToken token = this.getTokenInfoByTokenId(milkReq.getToken());
			if(!token.getIsValid().equals("YES"))
				throw new RuntimeException("Token has already been used");
			SeasonCustomer customer = getCustomerInfoByUniqueId(token.getBelongsTo());
			Double quantity = customer.getQuantityAllowed();
			
			if(tRecord==null) {
				throw new NullPointerException("Set Today's Record First");
			} else if(quantity < 0 ) {
				throw new NullPointerException("Negative Value is  not allowed as quantity. ");
			} else if(quantity > tRecord.getAvailableQuantity()) {
				throw new NullPointerException("Requesting quantity is greater than available milk quantity in the shop.");
			} else {
				double TotalPayableAmount = (tRecord.getCostPerLitre()) * quantity;
				String paymentMethod = milkReq.getPaymentMethod();
				AllTransactions transactions=new AllTransactions();
				if(paymentMethod.equalsIgnoreCase("Cash")) {
					//updating in customer table
					double spendAmount = customer.getPaidAmount();
					double updatedAmount = spendAmount + TotalPayableAmount;
					customer.setPaidAmount(updatedAmount);
				
				//updating todays record table
					Double moneyInCounter= tRecord.getCashInTheCounter();
					tRecord.setCashInTheCounter(moneyInCounter+TotalPayableAmount);
					tRecord.setAvailableQuantity(tRecord.getAvailableQuantity()-quantity);
					aqrepo.save(tRecord); //saved
					
				//updating in transactions table
					transactions.setDescription("Received cash from "+customer.getCustomerName());
					transactions.setReceivedAmount(TotalPayableAmount);
					
				//updating in earnins and pendings table
					EarningsAndPendings eap1=new EarningsAndPendings();
					EarningsAndPendings eap=this.earningsAndPendingsRepo.findByDate(parsedDate);
					if(eap==null) {
						eap1.setDate(parsedDate);
						eap1.setReceivedAmount(TotalPayableAmount);
						eap1.setPendingAmount(0.0);
						eap1.setSpendAmount(0.0);
						this.earningsAndPendingsRepo.save(eap1);
					} else {
						if(eap.getReceivedAmount()==null)
							eap.setReceivedAmount(TotalPayableAmount);
						else
							eap.setReceivedAmount(eap.getReceivedAmount()+TotalPayableAmount);
						this.earningsAndPendingsRepo.save(eap);
					}
					
				} else if(paymentMethod.equalsIgnoreCase("credit")){ //if payment is credit
				//updating in transactions table
					transactions.setDescription(customer.getCustomerName()+" with id "+customer.getUniqueCustId()+" has received "+quantity+" litres of milk using credit for "+TotalPayableAmount+" Rupees");
					transactions.setReceivedAmount(0.0);
				//updating in customers table
					double creditAmount = customer.getCreditAmount();
					double newAmount = creditAmount + TotalPayableAmount;
					customer.setCreditAmount(newAmount);
				//updating in todays record table
					tRecord.setAvailableQuantity(tRecord.getAvailableQuantity()-quantity);
					if(tRecord.getYetToReceive()==null)
						tRecord.setYetToReceive(TotalPayableAmount);
					else
						tRecord.setYetToReceive(tRecord.getYetToReceive()+TotalPayableAmount);
					tRecord.setLastUpdatedAt(getCurrentTime());
					aqrepo.save(tRecord); //saved
				//updating in ernings and pendings
					EarningsAndPendings eap1=new EarningsAndPendings();
					EarningsAndPendings eap=this.earningsAndPendingsRepo.findByDate(parsedDate);
					if(eap==null) {
						eap1.setDate(parsedDate);
						eap1.setPendingAmount(TotalPayableAmount);
						eap1.setReceivedAmount(0.0);
						eap1.setSpendAmount(0.0);
						this.earningsAndPendingsRepo.save(eap1);
					} else {
						if(eap.getPendingAmount()==null)
							eap.setPendingAmount(TotalPayableAmount);
						else
							eap.setPendingAmount(eap.getPendingAmount()+TotalPayableAmount);
						this.earningsAndPendingsRepo.save(eap);
					}
				}
				String date=getCurrentTime();
				
			//some common attributes for cash and credit, so we are setting commonly and updating in transactions table
				transactions.setCustomerId(customer.getUniqueCustId());
				transactions.setDate(parsedDate);
				transactions.setTime(LocalDateTime.now());
				transactions.setTransactionId(transactionId);
				transactions.setPaidAmount(0.0);
				transactionRepo.save(transactions); //saved	
			//some common attributes for cash and credit, so we are setting commonly and updating in customer table
				customer.setLastVisitedTime(getCurrentTime());
				double purchasedQuantity = customer.getPurchasedQuantity();
				double newPurchasedQuantity = purchasedQuantity + quantity;
				customer.setPurchasedQuantity(newPurchasedQuantity);
				this.srepo.save(customer);
				token.setIsValid("NO");
				this.trepo.save(token);
				return customer.getCustomerName() +" have received "+customer.getQuantityAllowed()+" ltrs today for Amount "+TotalPayableAmount+" on "+getCurrentTime();
		} 
		}catch (MilkServiceException e) {
			throw new MilkServiceException("Invalid Token Entered");
		}
	}
//serving milk for gguest users
	public String serveMilkForGuest(Double quantity) {
		String transactionId="TNS"+generateRandomID(5);
		
		TodaysRecord record=this.getRecord();
		Double money=quantity*record.getCostPerLitre();
		
		if(quantity>record.getAvailableQuantity())
			throw new IllegalArgumentException("Insufficient quantity");
		else if(quantity<=0)
			throw new IllegalArgumentException("Invalid quantity entered");
		else {
			//updating in todays record
			record.setCashInTheCounter(record.getCashInTheCounter()+money);
			record.setAvailableQuantity(record.getAvailableQuantity()-quantity);
			record.setLastUpdatedAt(getCurrentTime());
			this.aqrepo.save(record);
		//updating in transactions table
			AllTransactions transactions=new AllTransactions();
			transactions.setDescription("Received '"+money+"' cash from guest customer for "+quantity+" litres");
			transactions.setReceivedAmount(money);
			transactions.setDate(parsedDate);
			transactions.setCustomerId("---");
			transactions.setPaidAmount(0.0);
			transactions.setTime(LocalDateTime.now());
			transactions.setTransactionId(transactionId);
			this.transactionRepo.save(transactions);
		//updating in earnings and pendings table
			EarningsAndPendings eap1=new EarningsAndPendings();
			EarningsAndPendings eap=this.earningsAndPendingsRepo.findByDate(parsedDate);
			if(eap==null) {
				eap1.setDate(parsedDate);
				eap1.setPendingAmount(0.0);
				eap1.setReceivedAmount(money);
				eap1.setSpendAmount(0.0);
				this.earningsAndPendingsRepo.save(eap1);
			} else {
				if(eap.getReceivedAmount()==null)
					eap.setPendingAmount(money);
				else
					eap.setReceivedAmount(eap.getReceivedAmount()+money);
				this.earningsAndPendingsRepo.save(eap);
			}
			return "Received '"+quantity+"' litres of milk for '"+money+"' rupees, Visit again!!!";
		}
	}
	public String updatingAvailableQuantity(TodaysRecord record) {
		TodaysRecord t= this.getRecord();
		if(t==null)
			throw new NullPointerException("Set Record First To Update");
		else if(record.getAvailableQuantity()<=0)
			throw new RuntimeException("Invalid quantity entered");
		else {
			t.setAvailableQuantity(record.getAvailableQuantity());
			aqrepo.save(t);
			return "Available Quantity Updated Successfully";
		}
	}
	
	//if customer pays credit amount
	public String collectAmount(TodaysRecord record) {
		TodaysRecord t=this.getRecord();
		String id=record.getCustomerId();
		SeasonCustomer sc=srepo.findByUniqueCustId(id);
		if(sc==null) {
			throw new NullPointerException("No Customer Found With "+id);
		} else
		if(sc.getCreditAmount()==0) {
			throw new IllegalArgumentException("😂 no amount is pending from "+sc.getCustomerName());
		} else if(record.getReceivedAmount()>sc.getCreditAmount()) {
			throw new IllegalArgumentException("invalid amount entered, entered value shouldn't exceed "+sc.getCreditAmount());
		} else if(record.getReceivedAmount()<=0) {
			throw new IllegalArgumentException("invalid amount entered");
		} else {
			//updating in customers table
			sc.setCreditAmount(sc.getCreditAmount()-record.getReceivedAmount());
			sc.setPaidAmount(sc.getPaidAmount()+record.getReceivedAmount());
			srepo.save(sc);
	
			//updating in todays record
			if(t==null) {
				throw new NullPointerException("Set Record First 😡😡😡");
			}
			Double cashInTheCounter=t.getCashInTheCounter();
			t.setCashInTheCounter(cashInTheCounter+record.getReceivedAmount());
			t.setYetToReceive(t.getYetToReceive()-record.getReceivedAmount());
			t.setLastUpdatedAt(getCurrentTime());
			aqrepo.save(t);
			
			//updating in transactions table(should create new record)
			AllTransactions transactions=new AllTransactions();
			String transactionId="TNS"+generateRandomID(5);
			
			transactions.setTransactionId(transactionId);
			transactions.setCustomerId(sc.getUniqueCustId());
			transactions.setDescription("Received credit amount from "+id+" "+sc.getCustomerName());
			transactions.setReceivedAmount(record.getReceivedAmount());
			transactions.setPaidAmount(0.0);
			transactions.setDate(parsedDate);
			transactions.setTime(LocalDateTime.now());
			
			transactionRepo.save(transactions);
		//updating in earnings and pendings table
			EarningsAndPendings eap=this.earningsAndPendingsRepo.findByDate(parsedDate);
			EarningsAndPendings eap1=new EarningsAndPendings();
			if(eap==null) {
				eap1.setDate(parsedDate);
				eap1.setReceivedAmount(record.getReceivedAmount());
				eap1.setPendingAmount(0.0);
				eap1.setSpendAmount(0.0);
				this.earningsAndPendingsRepo.save(eap1);
			} else {
				if(eap.getReceivedAmount()==null)
					eap.setReceivedAmount(record.getReceivedAmount());
				else
					eap.setReceivedAmount(eap.getReceivedAmount()+record.getReceivedAmount());
				this.earningsAndPendingsRepo.save(eap);
			}
			return record.getReceivedAmount()+" rupees collected from "+id;
		}
	}
	
//paying any expenses
	public String raiseExpense(AllTransactions allTransactions, String paidTo, String name) {
		TodaysRecord record = getRecord();
		Double paidAmount = allTransactions.getPaidAmount();
		if (paidAmount <= 0) {
			throw new RuntimeException("Invalid Amount Entered");
		} else if (record.getCashInTheCounter() < paidAmount) {
			throw new RuntimeException("Insufficient Balance, Available Balance is " + record.getCashInTheCounter());
		} else {
		//updatingg in cattle maintainance record
			CattleRecords cattleRecord=new CattleRecords();
			if(paidTo.equalsIgnoreCase("vet")) {
				CattleDetails details=this.cattleDetailsRepo.findByName(name);
				if(details==null)
					throw new NullPointerException("No cattle found with name '"+name+"'");
				else {
					CattleRecords rec=this.cattleRecordsRepo.findByDateAndName(parsedDate, name);
					if(rec!=null) {
						rec.setVisitedVet("YES");
						cattleRecordsRepo.save(rec);
					} else {
						cattleRecord.setDate(parsedDate);
						cattleRecord.setName(name);
						cattleRecord.setVisitedVet("YES");
						cattleRecordsRepo.save(cattleRecord);
					}
				}
			}
			// updating in todays record
			record.setCashInTheCounter(record.getCashInTheCounter() - paidAmount);
			record.setLastUpdatedAt(getCurrentTime());
			aqrepo.save(record);

			// updating in all transactions
			String transactionId="TNS"+generateRandomID(5);
			
			allTransactions.setTransactionId(transactionId);
			allTransactions.setReceivedAmount(0.0);
			allTransactions.setCustomerId("---");
			allTransactions.setDate(parsedDate);
			allTransactions.setTime(LocalDateTime.now());
			transactionRepo.save(allTransactions);
			
			//updating in earnings and pendings table
			EarningsAndPendings eap=this.earningsAndPendingsRepo.findByDate(parsedDate);
			EarningsAndPendings eap1=new EarningsAndPendings();
			if(eap==null) {
				eap1.setDate(parsedDate);
				eap1.setSpendAmount(paidAmount);
				eap1.setPendingAmount(0.0);
				eap1.setReceivedAmount(0.0);
				this.earningsAndPendingsRepo.save(eap1);
			} else {
				if(eap.getSpendAmount()==null)
					eap.setSpendAmount(paidAmount);
				else
					eap.setSpendAmount(eap.getSpendAmount()+paidAmount);
				this.earningsAndPendingsRepo.save(eap);
			}
			return "Bill Paid Successfully With " + paidAmount + " rupees";
		}
	}
	

	//paying any expenses
		public String raiseExpenseBill(AllTransactions allTransactions) {
			TodaysRecord record = getRecord();
			Double paidAmount = allTransactions.getPaidAmount();
			if (paidAmount <= 0) {
				throw new RuntimeException("Invalid Amount Entered");
			} else if (record.getCashInTheCounter() < paidAmount) {
				throw new RuntimeException("Insufficient Balance, Available Balance is " + record.getCashInTheCounter());
			} else {
			
				// updating in todays record
				record.setCashInTheCounter(record.getCashInTheCounter() - paidAmount);
				record.setLastUpdatedAt(getCurrentTime());
				aqrepo.save(record);

				// updating in all transactions
				String transactionId="TNS"+generateRandomID(5);
				
				allTransactions.setTransactionId(transactionId);
				allTransactions.setReceivedAmount(0.0);
				allTransactions.setCustomerId("---");
				allTransactions.setDate(parsedDate);
				allTransactions.setTime(LocalDateTime.now());
				transactionRepo.save(allTransactions);
				
				//updating in earnings and pendings table
				EarningsAndPendings eap=this.earningsAndPendingsRepo.findByDate(parsedDate);
				EarningsAndPendings eap1=new EarningsAndPendings();
				if(eap==null) {
					eap1.setDate(parsedDate);
					eap1.setSpendAmount(paidAmount);
					eap1.setPendingAmount(0.0);
					eap1.setReceivedAmount(0.0);
					this.earningsAndPendingsRepo.save(eap1);
				} else {
					if(eap.getSpendAmount()==null)
						eap.setSpendAmount(paidAmount);
					else
						eap.setSpendAmount(eap.getSpendAmount()+paidAmount);
					this.earningsAndPendingsRepo.save(eap);
				}
				return "Bill Paid Successfully With " + paidAmount + " rupees";
			}
		}
//collections based on given date
	public String getAmountByDates(LocalDate date) {
		Double cash=0.0;
		List<AllTransactions> transactions=transactionRepo.findAllByDate(date);
		if(transactions.isEmpty())
			throw new NullPointerException("No transactions were made on given date");
		else
			for(AllTransactions list:transactions) {
				cash=cash+list.getReceivedAmount();
			}
		return "On '"+date+"' You have received "+cash+" rupees";
			
	}
//everyday earnings
	public List<EarningsAndPendings> getEarnings(){
		List<EarningsAndPendings> list=this.earningsAndPendingsRepo.findAll();
		if(list==null)
			throw new NullPointerException("This page is empty");
		else
			return list;
	}
//retrieving expense reports of specific days
	public List<AllTransactions> fetchTransactions(LocalDate fromDate, LocalDate toDate) {
		
		List<AllTransactions> transactionsBetweenDates=transactionRepo.findAllByDateBetween(fromDate, toDate);
		List<AllTransactions> list=new ArrayList<>();
		if(transactionsBetweenDates.size()==0) {//||transactionsBetweenDates.isEmpty()) {
			throw new NullPointerException("No Expenses Were Made Between "+fromDate+" and "+toDate);
		}
		else {
			for(AllTransactions transactions:transactionsBetweenDates) {
				if(transactions.getPaidAmount()>0) {
					list.add(transactions);
				}
			}
			if(list.isEmpty()) {
				throw new NullPointerException("No Expenses Were Made Between "+fromDate+" and "+toDate);
			} else
				return list;
		}
	}
	
	//deleting particular expense
	public String deleteExpense(String transactionId) {
		AllTransactions transaction=transactionRepo.findByTransactionId(transactionId);
		if(transaction==null) 
			throw new NullPointerException ("No Customer Found with transaction id "+transactionId);
		else if(transaction.getPaidAmount()>0) {
			Double paidAmount=transaction.getPaidAmount();
			TodaysRecord rec=this.getRecord();
			Double cashInCounter=rec.getCashInTheCounter();
			if(cashInCounter==null)
				rec.setCashInTheCounter(paidAmount);
			else
				rec.setCashInTheCounter(cashInCounter+paidAmount);
			aqrepo.save(rec);
			transactionRepo.delete(transaction);
			return "Transaction deleted successfully";
		} else
			throw new IllegalArgumentException("Can't delete customer transaction reports");
	}
//deleting the customer 	
	public String deleteCustomer(String customerId) {
		SeasonCustomer cd1 = srepo.findByUniqueCustId(customerId);
		if(cd1==null) {
			throw new NullPointerException ("No Customer Found with id "+customerId);
		} else if(cd1.getCreditAmount()>0) {
			throw new IllegalArgumentException("Can't delete "+cd1.getCustomerName() + " with id " + customerId + " because "+cd1.getCreditAmount()+" rupees pending from the customer");
		} else {
			String name=cd1.getCustomerName();
			srepo.delete(cd1);
			return "Customer "+name+" with id "+customerId+" deleted successfully";
		}
	}
//updating the customer
	public String updateCustomer(SeasonCustomer sCustomer) {
		try {
			SeasonCustomer customer=this.getCustomerInfoByUniqueId(sCustomer.getUniqueCustId());
			if(sCustomer.getMobileNumber()!=null) {
				if(sCustomer.getMobileNumber().length()!=10)
					throw new IllegalArgumentException("Mobile number should be equal to 10 digits");
				else 
					customer.setMobileNumber(sCustomer.getMobileNumber());
			}
			if(sCustomer.getQuantityAllowed()!=null) {
				if(sCustomer.getQuantityAllowed()<=0)
					throw new IllegalArgumentException("Allowed quantity should be greater than 0");
				else
					customer.setQuantityAllowed(sCustomer.getQuantityAllowed());
			}
			if(sCustomer.getAddress()!=null) 
				customer.setAddress(sCustomer.getAddress());
			 else if(sCustomer.getCustomerName()!=null)
				customer.setCustomerName(sCustomer.getCustomerName());
			srepo.save(customer);
			return "Customer with id '"+sCustomer.getUniqueCustId()+"' updated successfully";
		} catch(NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}
	}
}