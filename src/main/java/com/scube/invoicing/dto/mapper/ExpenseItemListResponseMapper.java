package com.scube.invoicing.dto.mapper;

import java.util.HashSet;
import java.util.Set;

import com.scube.invoicing.dto.ExpenseItemListResponseDto;
import com.scube.invoicing.entity.ExpenseCategoryItemListEntity;

public class ExpenseItemListResponseMapper {
	
	public static ExpenseItemListResponseDto toExpenseItemListResponseDto(ExpenseCategoryItemListEntity expenseCategoryItemListEntity) {
		return new ExpenseItemListResponseDto()
				.setExpenseItemID(expenseCategoryItemListEntity.getId())
				.setCategory(expenseCategoryItemListEntity.getCategoryMasterEntity().getExpenseCategoryName())
				.setDescription(expenseCategoryItemListEntity.getDescription())
				.setCustomer(expenseCategoryItemListEntity.getCustomerMasterEntity().getTitle()
						+ " " + expenseCategoryItemListEntity.getCustomerMasterEntity().getFirstName()
						+ " " + expenseCategoryItemListEntity.getCustomerMasterEntity().getLastName())
				.setTax(expenseCategoryItemListEntity.getGstMasterEntity().getValue());
	}
	
	public static Set<ExpenseItemListResponseDto> toExpenseResponseDtosList(Set<ExpenseCategoryItemListEntity> expenseCategoryItemListEntity2) {
		// TODO Auto-generated method stub
		
		Set<ExpenseItemListResponseDto> expenseItemListResponseDtosList = new HashSet<ExpenseItemListResponseDto>();
		for(ExpenseCategoryItemListEntity expenseCategoryItemListEntity : expenseCategoryItemListEntity2) {
			expenseItemListResponseDtosList.add(toExpenseItemListResponseDto(expenseCategoryItemListEntity));			
		}
				
		return expenseItemListResponseDtosList;
	}

}
