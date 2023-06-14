package com.anji.finance.savecc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anji.finance.savecc.dto.CreditCardRequest;
import com.anji.finance.savecc.dto.CreditCardResponse;
import com.anji.finance.savecc.dto.Error;
import com.anji.finance.savecc.service.CreditCardService;

/**
 * @author anjiboddupally
 *
 */

@RestController
@RequestMapping("/v1/cc")
public class CreditCardController {

	@Autowired
	CreditCardService ccService;

	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreditCardResponse> saveCC(@RequestBody CreditCardRequest dto) {
		try {
			return new ResponseEntity<CreditCardResponse>(ccService.saveCCDetails(dto), HttpStatus.OK);
		} catch (Exception eex) {
			
			Error err = Error.builder().message(eex.getMessage()).build();
			CreditCardResponse ccr = CreditCardResponse.builder().error(err).build();
			return new ResponseEntity<CreditCardResponse>(ccr, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
