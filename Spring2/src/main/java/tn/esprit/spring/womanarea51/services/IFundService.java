package tn.esprit.spring.womanarea51.services;

import java.util.List;

import tn.esprit.spring.womanarea51.entities.fund;

public interface IFundService {
	
	void AddFund(fund f);

	fund EditFund(fund f);

	void DeleteFund(fund f);
	
	fund FindFund(Long id);
	
	List<fund> ListFunds();
	
	public float estimatedAmountPerYear(int year);
	
	public float estimatedAmountforThisYear();
	

}