package com.cg.mypaymentapp.service;
import java.math.BigDecimal;
import java.sql.SQLException;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.exception.InvalidInputException;


public interface WalletService {
public Customer createAccount(String name ,String mobileno, BigDecimal amount);
	
public Customer showBalance (String mobileno) throws SQLException;
public Customer fundTransfer (String sourceMobileNo,String targetMobileNo, BigDecimal amount);
public Customer depositAmount (String mobileNo,BigDecimal amount ) throws  SQLException;
public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws SQLException;


}
