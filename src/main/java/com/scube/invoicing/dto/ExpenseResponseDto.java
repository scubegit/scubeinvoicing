package com.scube.invoicing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseResponseDto {

	private String expenseID;

	private VendorMasterResponseDto vendorInfo;
	private String expenseDate;
	private String paymentMethod;
	private String referenceNo;
	
	private String paymentAccount;
	
	private String roundOffAmount;
	private String subTotal;
	private String totalAmount;
	
	private String memo;
	
	private List<ExpenseItemListResponseDto> expenseItemList;
	
}
