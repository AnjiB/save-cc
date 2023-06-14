package com.anji.finance.savecc.fraud;

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
public class FraudProps {
	
	private String issuer;
	
	private int fraudScore;

}
