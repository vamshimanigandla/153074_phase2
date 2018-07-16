package com.cg.mypaymentapp.repo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.util.ConnectionSQL;

public class WalletRepoImpl implements WalletRepo {

	private Map<String, Customer> data;

	public WalletRepoImpl(Map<String, Customer> data) {
		super();
		this.data = data;
	}

	public boolean save(Customer customer) {

		if (customer == null)
			return false;
		else {
			/*data.put(customer.getMobileNo(), customer);*/   /* data put into hashmap*/
			try {
				String query="insert into customer values(?,?,?)";
				PreparedStatement preparedStatement=ConnectionSQL.con.prepareStatement(query);
				preparedStatement.setString(1, customer.getName());
				preparedStatement.setString(2,customer.getMobileNo());
				preparedStatement.setInt(3, customer.getWallet().getBalance().intValue());
				
				preparedStatement.execute();
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			return true;
		}

	}

	public Customer findOne(String mobileNo) throws SQLException {
		

		/*if (data.containsKey(mobileNo))
			return data.get(mobileNo);
			return null;*/
		
		
		Customer customer=new Customer();
		String query="select * from customer where mobileno=?";
		PreparedStatement preparedStatement=ConnectionSQL.con.prepareStatement(query);
		preparedStatement.setString(1, mobileNo);
		ResultSet rs=preparedStatement.executeQuery();
		 while(rs.next())
		 {
			 if(rs.getString(2).equals(mobileNo))
			 {
				 customer.setMobileNo(rs.getString(2));
				 customer.setName(rs.getString(1));
				 BigDecimal b=new BigDecimal(rs.getInt(3));
				 customer.setWallet(new Wallet(b));
				 
			 }
		 }

		return customer;
		
	}

	@Override
	public void update(Customer customer) throws SQLException {
		// TODO Auto-generated method stub
		
		String query="update customer set amount=? where mobileno=?";
		PreparedStatement preparedstatement=ConnectionSQL.con.prepareStatement(query);
		
		preparedstatement.setBigDecimal(1, customer.getWallet().getBalance());
		
		preparedstatement.setString(2, customer.getMobileNo());
		preparedstatement.executeUpdate();
		
		
		
		
		
	}

	
	
	
	
	
	
	
	
}
