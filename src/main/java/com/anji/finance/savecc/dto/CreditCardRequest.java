package com.anji.finance.savecc.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

/**
 * @author anjiboddupally
 *
 */

@Data
@Builder
public class CreditCardRequest {
	
	@NotNull
	private String number;
	
	@NotNull
	private LocalDate expDate;
	
	private LocalDate issueDate;
	
	@NotNull
	private int cvc;
	
	@NotNull
	private String ccHolderName;
	
}
