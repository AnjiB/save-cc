package com.anji.finance.savecc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anji.finance.savecc.entity.CreditCard;

/**
 * @author anjiboddupally
 *
 */
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	
}
