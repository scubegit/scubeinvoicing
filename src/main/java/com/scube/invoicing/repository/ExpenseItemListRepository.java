package com.scube.invoicing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scube.invoicing.entity.ExpenseCategoryItemListEntity;
import com.scube.invoicing.entity.ExpenseInfoEntity;

@Repository
public interface ExpenseItemListRepository extends JpaRepository<ExpenseCategoryItemListEntity, String> {
	
	List<ExpenseCategoryItemListEntity> findByExpenseInfoEntity(ExpenseInfoEntity expenseInfoEntity);
	


}
