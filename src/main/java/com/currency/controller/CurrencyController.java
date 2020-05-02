package com.currency.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.currency.dao.CurrencyExchangeDAO;
import com.currency.model.CurrencyExchange;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RefreshScope
public class CurrencyController {
	
	@Autowired
	CurrencyExchangeDAO daoExchange;
	
	private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
	
	@RequestMapping("/addCurrency")
	public String addCurrency(CurrencyExchange objExchange)
	{
		String status = "";
		logger.info("endpoint /addCurrency -> Entry");
		try {
			daoExchange.save(objExchange);
			status = "data added";
		}catch(Exception e)
		{
			logger.error(e.getStackTrace().toString());
			status ="error in data to be added";
		}
		
		logger.info("endpoint /addCurrency -> Exit");
		return status;
	}
	
	
	@RequestMapping("/updateCurrency")
	public String updateCurrency(CurrencyExchange objExchange)
	{
		String status = "Nothing to update";
		CurrencyExchange objExchangeFromDB = null;
		logger.info("endpoint /updateCurrency -> Entry");
		boolean isExists = daoExchange.existsById(objExchange.getCountryCode());
		if(isExists)
		{
			objExchangeFromDB = daoExchange.findById(objExchange.getCountryCode()).get();
			objExchangeFromDB.setConversionRate(objExchange.getConversionRate());
			daoExchange.save(objExchangeFromDB);
			status ="Updated Currency Exchange rate";
			
		}
		logger.info("endpoint /updateCurrency -> Exit");
		return status;
	}
	
	@RequestMapping("/getCurrency")
	public List<CurrencyExchange> getExchangeRate(String countryCode)
	{
		logger.info("endpoint /getCurrency -> Entry");
		
		List<CurrencyExchange> objExchangeFromDB = new  ArrayList();
		
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
		
		logger.info("endpoint /getCurrency -> exit");
		
		return objExchangeFromDB;
		
	}
	
	@RequestMapping("/convertCurrency")
	@HystrixCommand(fallbackMethod = "convertCurrencyFallback")
	public double convertCurrency(String countryCode,Double amount)
	{
		CurrencyExchange objExchangeFromDB = null;
		double exchangedAmount = 0.0;
		logger.info("endpoint /convertCurrency -> Entry code " + countryCode +"amount = "+amount);
		
		if (countryCode != null )
		{
			logger.info("in not null country code");
			boolean isExists = daoExchange.existsById(countryCode);
			if(isExists)
			{
				objExchangeFromDB = daoExchange.findById(countryCode).get();
				exchangedAmount = objExchangeFromDB.getConversionRate()*amount;
				logger.info("in not null country code" + exchangedAmount + "and value from db = "+exchangedAmount);
				
			}
		}
		
		
		logger.info("endpoint /convertCurrency -> exit");
		return exchangedAmount;
		
	}
	
	public double convertCurrencyFallback(String countryCode,Double amount)
	{
		return 0;
	}
	


}
