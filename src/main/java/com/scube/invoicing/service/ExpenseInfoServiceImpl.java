package com.scube.invoicing.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scube.invoicing.dto.ExpenseResponseDto;
import com.scube.invoicing.dto.incoming.ExpenseIncomingDto;
import com.scube.invoicing.dto.incoming.ExpenseItemListIncomingDto;
import com.scube.invoicing.dto.mapper.ExpenseInfoAndItemListMapper;
import com.scube.invoicing.entity.ExpenseCategoryItemListEntity;
import com.scube.invoicing.entity.CategoryMasterEntity;
import com.scube.invoicing.entity.CustomerMasterEntity;
import com.scube.invoicing.entity.ExpenseInfoEntity;
import com.scube.invoicing.entity.GSTMasterEntity;
import com.scube.invoicing.entity.PaymentMethodEntity;
import com.scube.invoicing.entity.VendorMasterEntity;
import com.scube.invoicing.exception.BRSException;
import com.scube.invoicing.repository.ExpenseInfoRepository;
import com.scube.invoicing.repository.ExpenseItemListRepository;
import com.scube.invoicing.util.DateUtils;
import com.scube.invoicing.util.RandomUtils;

@Service
public class ExpenseInfoServiceImpl implements ExpenseInfoService {
	
	@Autowired
	GSTMasterService gstMasterService;
	
	@Autowired
	CustomerMasterService customerMasterService;
	
	@Autowired
	CategoryMasterService categoryMasterService;
	
	@Autowired
	VendorMasterService vendorMasterService;
	
	@Autowired
	PaymentMethodService paymentMethodService;
	
	@Autowired
	ExpenseInfoRepository expenseInfoRepository;
	
	@Autowired
	ExpenseItemListRepository expenseItemListRepository;
	
	Base64.Encoder encoder = Base64.getEncoder();
	Base64.Decoder decoder = Base64.getDecoder();
	
	private static final Logger logger = LoggerFactory.getLogger(ExpenseInfoServiceImpl.class);

	@Override
	public boolean createNewExpense(@Valid ExpenseIncomingDto expenseIncomingDto) {
		// TODO Auto-generated method stub
		logger.info("------- ExpenseInfoController createNewExpense ------");
		
		if(expenseIncomingDto.getVendorName() == "" || expenseIncomingDto.getVendorName().trim().isEmpty()) {
			throw BRSException.throwException("Error : Vendor Name cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getPaymentAccount() == "" || expenseIncomingDto.getPaymentAccount().trim().isEmpty()) {
			throw BRSException.throwException("Error : Company Payment Account cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getPaymentDate() == "" || expenseIncomingDto.getPaymentDate().trim().isEmpty()) {
			throw BRSException.throwException("Error : Payment Date cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getPaymentMethod() == "" || expenseIncomingDto.getPaymentMethod().trim().isEmpty()) {
			throw BRSException.throwException("Error : Payment Method cannot be empty or blank");
		}
/*		
		if(expenseIncomingDto.getReferenceNo() == "" || expenseIncomingDto.getReferenceNo().trim().isEmpty()) {
			throw BRSException.throwException("Error : Reference No cannot be empty or blank");
		}
*/		
		VendorMasterEntity vendorMasterEntity = vendorMasterService.getVendorMasterEntityByVendorID(expenseIncomingDto.getVendorName());
		
		PaymentMethodEntity paymentMethodEntity = paymentMethodService.getPaymentMethodEntityByPaymentMethodID(expenseIncomingDto.getPaymentMethod());
		
		ExpenseInfoEntity expenseInfoEntity = new ExpenseInfoEntity();
		expenseInfoEntity.setIsdeleted("N");
		expenseInfoEntity.setPaymentDate(DateUtils.stringToDateConvert(expenseIncomingDto.getPaymentDate()));
		expenseInfoEntity.setReferenceNo("EXPENSE-00" + RandomUtils.generateRandomNumber());
		expenseInfoEntity.setPaymentAccount(expenseIncomingDto.getPaymentAccount());
		
		// Sub-total/Total/Round off Amount
		expenseInfoEntity.setSubTotal(encoder.encodeToString(expenseIncomingDto.getSubTotal().getBytes(StandardCharsets.UTF_8)));
		expenseInfoEntity.setTotalAmount(encoder.encodeToString(expenseIncomingDto.getTotalAmount().getBytes(StandardCharsets.UTF_8)));
		expenseInfoEntity.setRoundOffAmount(encoder.encodeToString(expenseIncomingDto.getRoundOffAmount().getBytes(StandardCharsets.UTF_8)));
		
		// Memo
		expenseInfoEntity.setMemo(expenseIncomingDto.getMemo() != null ?expenseIncomingDto.getMemo() : null);
		
		expenseInfoEntity.setVendorMasterEntity(vendorMasterEntity);
		expenseInfoEntity.setPaymentMethodEntity(paymentMethodEntity);
		
		expenseInfoRepository.save(expenseInfoEntity);
		
		Set<ExpenseCategoryItemListEntity> expenseCategoryItemListEntitiesList = new HashSet<ExpenseCategoryItemListEntity>();
		
		for(ExpenseItemListIncomingDto expenseItemListIncomingDto : expenseIncomingDto.getExpenseItemListIncomingDtos()) {
			
			CustomerMasterEntity customerMasterEntity = customerMasterService.
					getCustomerDetailsByCustomerId(expenseItemListIncomingDto.getCustomer());
			CategoryMasterEntity categoryMasterEntity = categoryMasterService.
					getCategoryMasterEntityByCategoryID(expenseItemListIncomingDto.getCategory());
			GSTMasterEntity gstMasterEntity = gstMasterService.getGstMasterEntityByGstID(expenseItemListIncomingDto.getTax());
			
			ExpenseCategoryItemListEntity expenseCategoryItemListEntity = new ExpenseCategoryItemListEntity();
			expenseCategoryItemListEntity.setIsdeleted("N");
			// Amount and Service Amount with GST
			expenseCategoryItemListEntity.setAmount(encoder.encodeToString(expenseItemListIncomingDto.getAmount()
					.getBytes(StandardCharsets.UTF_8)));
			expenseCategoryItemListEntity.setServiceAmountWithGst(encoder.encodeToString(expenseItemListIncomingDto.getServiceAmountWithGst()
					.getBytes(StandardCharsets.UTF_8)));
			
			// Category Description
			expenseCategoryItemListEntity.setDescription(expenseItemListIncomingDto.getDescription());
			
			// Customer/Category/Expense/GST Entity
			expenseCategoryItemListEntity.setCustomerMasterEntity(customerMasterEntity);
			expenseCategoryItemListEntity.setCategoryMasterEntity(categoryMasterEntity);
			expenseCategoryItemListEntity.setExpenseInfoEntity(expenseInfoEntity);
			expenseCategoryItemListEntity.setGstMasterEntity(gstMasterEntity);
			
			expenseCategoryItemListEntitiesList.add(expenseCategoryItemListEntity);
			
		}
		
		expenseItemListRepository.saveAll(expenseCategoryItemListEntitiesList);
		
		logger.info("------- Record Created Successfully ------");
		
		return true;
	}

	@Override
	public boolean updateExpense(@Valid ExpenseIncomingDto expenseIncomingDto) {
		// TODO Auto-generated method stub
		logger.info("------- ExpenseInfoController updateExpense ------");
		
		if(expenseIncomingDto.getVendorName() == "" || expenseIncomingDto.getVendorName().trim().isEmpty()) {
			throw BRSException.throwException("Error : Vendor Name cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getPaymentAccount() == "" || expenseIncomingDto.getPaymentAccount().trim().isEmpty()) {
			throw BRSException.throwException("Error : Company Payment Account cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getPaymentDate() == "" || expenseIncomingDto.getPaymentDate().trim().isEmpty()) {
			throw BRSException.throwException("Error : Payment Date cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getPaymentMethod() == "" || expenseIncomingDto.getPaymentMethod().trim().isEmpty()) {
			throw BRSException.throwException("Error : Payment Method cannot be empty or blank");
		}
		
		if(expenseIncomingDto.getReferenceNo() == "" || expenseIncomingDto.getReferenceNo().trim().isEmpty()) {
			throw BRSException.throwException("Error : Reference No cannot be empty or blank");
		}
		
		VendorMasterEntity vendorMasterEntity = vendorMasterService.getVendorMasterEntityByVendorID(expenseIncomingDto.getVendorName());
		
		PaymentMethodEntity paymentMethodEntity = paymentMethodService.getPaymentMethodEntityByPaymentMethodID(expenseIncomingDto.getPaymentMethod());
		
		ExpenseInfoEntity expenseInfoEntity = expenseInfoRepository.findById(expenseIncomingDto.getExpenseID()).get();
		expenseInfoEntity.setIsdeleted("N");
		expenseInfoEntity.setPaymentDate(DateUtils.stringToDateConvert(expenseIncomingDto.getPaymentDate()));
		expenseInfoEntity.setPaymentAccount(expenseIncomingDto.getPaymentAccount());
		
		// Sub-total/ Total Amount/Round off Amount
		expenseInfoEntity.setSubTotal(encoder.encodeToString(expenseIncomingDto.getSubTotal().getBytes(StandardCharsets.UTF_8)));
		expenseInfoEntity.setTotalAmount(encoder.encodeToString(expenseIncomingDto.getTotalAmount().getBytes(StandardCharsets.UTF_8)));
		expenseInfoEntity.setRoundOffAmount(encoder.encodeToString(expenseIncomingDto.getRoundOffAmount().getBytes(StandardCharsets.UTF_8)));
		
		// Memo
		expenseInfoEntity.setMemo(expenseIncomingDto.getMemo() != null ? expenseIncomingDto.getMemo() : null);
		
		expenseInfoEntity.setVendorMasterEntity(vendorMasterEntity);
		expenseInfoEntity.setPaymentMethodEntity(paymentMethodEntity);
		
		expenseInfoRepository.save(expenseInfoEntity);
		
		List<ExpenseCategoryItemListEntity> expenseCategoryItemListEntityList = 
				expenseItemListRepository.findByExpenseInfoEntity(expenseInfoEntity);
		
		expenseItemListRepository.deleteAll(expenseCategoryItemListEntityList);
		
		Set<ExpenseCategoryItemListEntity> expenseCategoryItemListEntitiesList = new HashSet<ExpenseCategoryItemListEntity>();
		
		for(ExpenseItemListIncomingDto expenseItemListIncomingDto : expenseIncomingDto.getExpenseItemListIncomingDtos()) {
			
			CustomerMasterEntity customerMasterEntity = customerMasterService.
					getCustomerDetailsByCustomerId(expenseItemListIncomingDto.getCustomer());
			CategoryMasterEntity categoryMasterEntity = categoryMasterService.
					getCategoryMasterEntityByCategoryID(expenseItemListIncomingDto.getCategory());
			GSTMasterEntity gstMasterEntity = gstMasterService.getGstMasterEntityByGstID(expenseItemListIncomingDto.getTax());
			
			ExpenseCategoryItemListEntity expenseCategoryItemListEntity = expenseItemListRepository.findById(
					expenseItemListIncomingDto.getExpenseItemID()).get();
			
			expenseCategoryItemListEntity.setIsdeleted("N");
			
			expenseCategoryItemListEntity.setAmount(encoder.encodeToString(expenseItemListIncomingDto.getAmount()
					.getBytes(StandardCharsets.UTF_8)));
			expenseCategoryItemListEntity.setServiceAmountWithGst(encoder.encodeToString(expenseItemListIncomingDto.getServiceAmountWithGst()
					.getBytes(StandardCharsets.UTF_8)));
			
			expenseCategoryItemListEntity.setDescription(expenseItemListIncomingDto.getDescription());
			expenseCategoryItemListEntity.setCustomerMasterEntity(customerMasterEntity);
			expenseCategoryItemListEntity.setCategoryMasterEntity(categoryMasterEntity);
			expenseCategoryItemListEntity.setExpenseInfoEntity(expenseInfoEntity);
			expenseCategoryItemListEntity.setGstMasterEntity(gstMasterEntity);
			
			expenseCategoryItemListEntitiesList.add(expenseCategoryItemListEntity);
			
		}
		
		expenseItemListRepository.saveAll(expenseCategoryItemListEntitiesList);
		
		logger.info("------- Record Update Successfully ------");
		
		return true;
	}

	@Override
	public boolean deleteExpenseByExpenseID(String expenseID) {
		// TODO Auto-generated method stub
		logger.info("------- ExpenseInfoController deleteExpenseByExpenseID ------");
		
		ExpenseInfoEntity expenseInfoEntity = expenseInfoRepository.findById(expenseID).get();
		List<ExpenseCategoryItemListEntity> expenseCategoryItemListEntityList = 
				expenseItemListRepository.findByExpenseInfoEntity(expenseInfoEntity);
		
		expenseItemListRepository.deleteAll(expenseCategoryItemListEntityList);
		expenseInfoRepository.delete(expenseInfoEntity);
		
		logger.info("------- Record Deleted Successfully ------");
		
		return true;
	}

	@Override
	public ExpenseResponseDto getExpenseInfoByExpenseID(String expenseID) {
		// TODO Auto-generated method stub
		logger.info("------- ExpenseInfoController getExpenseInfoByExpenseID ------");
		
		ExpenseInfoEntity expenseInfoEntity = expenseInfoRepository.findById(expenseID).get();
		List<ExpenseCategoryItemListEntity> expenseCategoryItemListEntityList = expenseItemListRepository.findByExpenseInfoEntity(expenseInfoEntity);
		
		return ExpenseInfoAndItemListMapper.toExpenseAndExpenseItemListResponseDto(expenseInfoEntity, expenseCategoryItemListEntityList);
	}

	@Override
	public List<ExpenseResponseDto> getAllExpenseList() {
		// TODO Auto-generated method stub
		logger.info("------- ExpenseInfoController getAllExpenseList ------");
		
		List<ExpenseInfoEntity> expenseInfoEntityList = expenseInfoRepository.findAll();

		return ExpenseInfoAndItemListMapper.toExpenseResponseDtosList(expenseInfoEntityList);
	}

}
