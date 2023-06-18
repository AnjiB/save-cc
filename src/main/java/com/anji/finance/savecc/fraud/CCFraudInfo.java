package com.anji.finance.savecc.fraud;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author anjiboddupally
 *
 */

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class CCFraudInfo extends Error {

	private String ccNumber;
	
	private LocalDate issueDate;

	private LocalDate expDate;
	
	private FraudProps fraudProps;

}
