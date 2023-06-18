package com.anji.finance.savecc.fraud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

	private String timestamp;
	
	private Integer status;
	
	private String error;
	
	private String path;
}
