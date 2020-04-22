package com.currency.dao;

import org.springframework.data.repository.CrudRepository;

import com.currency.model.CurrencyExchange;

public interface  CurrencyExchangeDAO extends CrudRepository<CurrencyExchange, String>{

}
