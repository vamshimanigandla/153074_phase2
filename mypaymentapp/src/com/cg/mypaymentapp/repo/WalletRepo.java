package com.cg.mypaymentapp.repo;

import java.sql.SQLException;

import com.cg.mypaymentapp.beans.Customer;

public interface WalletRepo {

	public boolean save(Customer customer);

	public Customer findOne(String mobileNo) throws SQLException;

	public void update(Customer customer) throws SQLException;

}
