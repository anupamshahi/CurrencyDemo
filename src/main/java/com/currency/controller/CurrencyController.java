package com.currency.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.currency.dao.CurrencyExchangeDAO;
import com.currency.model.CurrencyExchange;

@RestController
public class CurrencyController {
	
	@Autowired
	CurrencyExchangeDAO daoExchange;
	
	@RequestMapping("/addCurrency")
	public String addCurrency(CurrencyExchange objExchange)
	{
		System.out.println("hello world");
		daoExchange.save(objExchange);
		System.out.println("hello world out");
		return "added Currency  ";
	}
	
	@RequestMapping("/updateCurrency")
	public String updateCurrency(CurrencyExchange objExchange)
	{
		CurrencyExchange objExchangeFromDB = null;
		System.out.println("hello world");
		boolean isExists = daoExchange.existsById(objExchange.getCountryCode());
		if(isExists)
		{
			objExchangeFromDB = daoExchange.findById(objExchange.getCountryCode()).get();
			objExchangeFromDB.setConversionRate(objExchange.getConversionRate());
			daoExchange.save(objExchangeFromDB);
		}
		System.out.println("hello world out");
		return "hello world  no currencyfound to update";
	}
	
	@RequestMapping("/getCurrency")
	public List<CurrencyExchange> getExchangeRate(String countryCode)
	{
		List<CurrencyExchange> objExchangeFromDB = new  ArrayList();
		
		System.out.println("hello world");
		if (countryCode != null )
		{
			boolean isExists = daoExchange.existsById(countryCode);
			if(isExists)
			{
				objExchangeFromDB.add(daoExchange.findById(countryCode).get());
				
				
			}
		}
		
		else 
		{
			Iterable<CurrencyExchange> objsExchange  = daoExchange.findAll();
			objsExchange.forEach(objExchangeFromDB::add);
			
		}
		System.out.println("hello world out");
		return objExchangeFromDB;
		
	}
	
	@RequestMapping("/convertCurrency")
	public double convertCurrency(String countryCode,double amount)
	{
		CurrencyExchange objExchangeFromDB = null;
		double exchangedAmount = 0.0;
		
		System.out.println("in  convertCurrency");
		if (countryCode != null )
		{
			boolean isExists = daoExchange.existsById(countryCode);
			if(isExists)
			{
				objExchangeFromDB = daoExchange.findById(countryCode).get();
				exchangedAmount = objExchangeFromDB.getConversionRate()*amount;
				
				
			}
		}
		
		
		System.out.println("out  convertCurrency");
		return exchangedAmount;
		
	}
	


}
