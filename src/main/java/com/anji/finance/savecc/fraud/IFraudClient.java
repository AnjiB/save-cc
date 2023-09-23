package com.anji.finance.savecc.fraud;

import java.time.LocalDate;


/**
 * @author anjiboddupally
 *
 */


public interface IFraudClient {

	CCFraudInfo getFraudInfo(String ccNo, LocalDate expDate);
}
