package com.anji.finance.savecc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.finance.savecc.dto.CreditCardRequest;
import com.anji.finance.savecc.dto.CreditCardResponse;
import com.anji.finance.savecc.entity.CreditCard;
import com.anji.finance.savecc.fraud.CCFraudInfo;
import com.anji.finance.savecc.fraud.FraudClient;
import com.anji.finance.savecc.repository.CreditCardRepository;

/**
 * @author anjiboddupally
 *
 */

@Service
public class CreditCardService {

	@Autowired
	private CreditCardRepository ccRepository;

	@Autowired
	private FraudClient fraudClient;

	public CreditCardResponse saveCCDetails(CreditCardRequest ccReq) throws Exception {

		CreditCard cc = toEntity(ccReq);

		CCFraudInfo ccFInfo = fraudClient.getFraudInfo(ccReq.getNumber(), ccReq.getExpDate());

		cc.setIssueDate(ccFInfo.getIssueDate());

		if (ccFInfo.getFraudProps().getFraudScore() < 500) {
			throw new Exception("Fraud detected!!");
		}

		CreditCard xyz = ccRepository.save(cc);

		return toResponse(xyz);
	}

	private CreditCard toEntity(CreditCardRequest ccReq) {

		CreditCard cc = new CreditCard();
		cc.setCcHolderName(ccReq.getCcHolderName());
		cc.setCvc(ccReq.getCvc());
		cc.setExpDate(ccReq.getExpDate());
		cc.setNumber(ccReq.getNumber());

		return cc;

	}

	private CreditCardResponse toResponse(CreditCard cc) {

		return CreditCardResponse.builder().ccHolderName(cc.getCcHolderName()).cvc(cc.getCvc()).expDate(cc.getExpDate())
				.number(cc.getNumber()).issueDate(cc.getIssueDate()).build();

	}

}
