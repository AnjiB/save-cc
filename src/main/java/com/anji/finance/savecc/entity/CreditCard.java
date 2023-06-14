package com.anji.finance.savecc.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author anjiboddupally
 *
 */

@Entity
@Table(name = "CreditCard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreditCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "expDate")
	private LocalDate expDate;
	
	@Column(name = "issueDate")
	private LocalDate issueDate;
	
	@Column(name = "cvc")
	private int cvc;
	
	@Column(name = "ccHolderName")
	private String ccHolderName;

}
