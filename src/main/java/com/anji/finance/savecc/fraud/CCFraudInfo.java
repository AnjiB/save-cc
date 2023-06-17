package com.anji.finance.savecc.fraud;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anjiboddupally
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CCFraudInfo {

	private String ccNumber;
	
	private LocalDate issueDate;

	private LocalDate expDate;
	
	private FraudProps fraudProps;
	
	private Error error;
}
