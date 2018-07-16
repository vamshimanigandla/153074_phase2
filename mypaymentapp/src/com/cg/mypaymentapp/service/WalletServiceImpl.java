
package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {

	private WalletRepo repo;

	public WalletServiceImpl(Map<String, Customer> data) {
		repo = new WalletRepoImpl(data);
	}

	/*public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}

	*/

	@Override
	public Customer createAccount(String name, String mobileno, BigDecimal amount)  {
		// TODO Auto-generated method stub

		Scanner console = new Scanner(System.in);
		
		
		while(true)
		{
			try {
				if(validateName(name))
					break;
			}catch(InvalidInputException e){
				System.err.println(e.getMessage());
			}
			name=console.next();
		}
		
		
		while(true)
		{
			try {
				if(validateMobile(mobileno))
					break;
			}catch(InvalidInputException e){
				System.err.println(e.getMessage());
			}
			mobileno=console.next();
		}
		
		
		while(true)
		{
			try {
				if(validateBalance(amount))
					break;
			}catch(InvalidInputException e){
				System.err.println(e.getMessage());
			}
			amount=console.nextBigDecimal();
		}
				Customer customer = new Customer(name, mobileno, new Wallet(amount));
				boolean result = repo.save(customer);
				
				if (result == true)
					return customer;
				else
					return null;
	}

	private boolean validateMobile(String mobileno) {
		String pattern = "[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
		if (mobileno.matches(pattern)) {
			return true;
		} else
			throw new InvalidInputException("Enter mobile no of 10 digits");

	}
	
	
	private boolean validateName(String name) {
		String pattern = "[A-Z][a-z]*";
		if (name.matches(pattern)) {
			return true;
		} else
			throw new InvalidInputException("Name should start with a capital letter");

	}
	

	private boolean validateBalance(BigDecimal amount) {
		
		if (amount.intValue()>=0) {
			return true;
		} else
			throw new InvalidInputException("the amount should be greater than zero");

	}
	


	@Override
	public Customer showBalance(String mobileno) throws SQLException  {

		try{
			
		
		Customer customer1 = repo.findOne(mobileno);
	
			if (customer1 != null)
				return customer1;
		}
		catch(SQLException e)
		{
		System.err.println("Wrong Mobile no");
		}
		return null;
	
	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount)
			throws InvalidInputException {
		// TODO Auto-generated method stub

		try {
			if(amount.intValue()<0)
			{
				throw new InvalidInputException("Amount to transfer should be greater than zero");
			}
			
			int flag1 = 0;
			int flag2 = 0;

			if (repo.findOne(sourceMobileNo) != null)
				flag1 = 1;
			if (repo.findOne(targetMobileNo) != null)
				flag2 = 1;

			Customer sourcecustomer = repo.findOne(sourceMobileNo);
			Customer targetcustomer = repo.findOne(targetMobileNo);
			
			if(sourcecustomer.getWallet().getBalance().intValue()<amount.intValue())
			{
				
				System.out.println("Insufficient balance to transfer");
				//throw new InvalidInputException("Insufficient balance to transfer");
			}
			if (flag1 == 1 && flag2 == 1)
			{
				
				Customer remainingAmount=withdrawAmount(sourceMobileNo,amount);
				depositAmount(targetMobileNo,amount);
				return remainingAmount;
				

			} else if (flag1 == 0) {
				System.out.println("sender mobile no is invalid !!!!");
			} else if (flag2 == 0) {
				System.out.println("benefiaciary mobile no is invalid!!!!!");
			}
			
			
			
		} catch (Exception e) {
			System.out.println("invalid input details;");
		}
		return null;
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws  SQLException {
		// TODO Auto-generated method stub
	
			Customer dcustomer = repo.findOne(mobileNo);
			if (dcustomer != null) {
				Wallet iAmount = dcustomer.getWallet();
				BigDecimal updatedBalance = iAmount.getBalance().add(amount);
				Wallet wallet = new Wallet(updatedBalance);
				dcustomer.setWallet(wallet);
				repo.update(dcustomer);
				return dcustomer;
			}

		
		return null;

	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws SQLException {
		// TODO Auto-generated method stub
	if(validateMobile(mobileNo))
	{
		
			Customer wcustomer = repo.findOne(mobileNo);
			
			if (wcustomer != null) {
				
				Wallet iAmount = wcustomer.getWallet();
				BigDecimal updatedBalance = iAmount.getBalance().subtract(amount);
				
				Wallet wallet = new Wallet(updatedBalance);
				wcustomer.setWallet(wallet);
				
				repo.update(wcustomer);
				return wcustomer;
			}
	} 
		return null;

	}

}
