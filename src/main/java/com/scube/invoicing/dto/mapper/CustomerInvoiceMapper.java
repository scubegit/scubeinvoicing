package com.scube.invoicing.dto.mapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scube.invoicing.dto.CustomerInvoiceResponseDto;
import com.scube.invoicing.entity.CompanyMasterEntity;
import com.scube.invoicing.entity.CustomerInvoiceEntity;
import com.scube.invoicing.entity.CustomerInvoiceServiceEntity;
import com.scube.invoicing.util.DateUtils;
import com.scube.invoicing.util.StringNullEmpty;

public class CustomerInvoiceMapper {
	
	static Base64.Decoder decoder = Base64.getDecoder();

	public static CustomerInvoiceResponseDto toCustomerServiceAndCompanyResponseMailDto(
			CustomerInvoiceEntity customerInvoiceEntity, CompanyMasterEntity companyMasterEntity) {
		
		String mailBody = "Please find the Invoice. If you have any clarification kindly contact." 
				+ "Thanks for your Business!,"
				+ "Please click link below to view or download the invoice."
				+ companyMasterEntity.getCompanyName();
		
		return new CustomerInvoiceResponseDto()
				// Company Info and Mail Info
				.setCompanyName(companyMasterEntity.getCompanyName())
				.setToEmailID(customerInvoiceEntity.getCustEmailId() != null 
					? customerInvoiceEntity.getCustEmailId() : customerInvoiceEntity.getCustomerMasterEntity().getEmailId())
				.setFromEmailID(companyMasterEntity.getEmailId())
				.setBccEmailID(companyMasterEntity.getCompanyFinancialAccountant())
				.setSubject(customerInvoiceEntity.getInvoiceNo() + " from " + 
						companyMasterEntity.getCompanyName())
				.setMailBody(mailBody)
				.setCc(companyMasterEntity.getCc())
				
				// Customer Info
				.setCustomerID(customerInvoiceEntity.getCustomerMasterEntity().getId())
				.setCustEmailId(customerInvoiceEntity.getCustEmailId())
				.setCustomerBillingAddress(customerInvoiceEntity.getCustomerBillingAddress())
				.setShippingDate(DateUtils.formatDateToDDMMYYYYFormat(customerInvoiceEntity.getShippingDate()))
				.setShippingTo(customerInvoiceEntity.getShippingTo())
				.setShippingVia(customerInvoiceEntity.getShippingVia())
				.setTerms(customerInvoiceEntity.getTerms())
				
				// Invoice No and Invoice Date
				.setInvoiceNo(customerInvoiceEntity.getInvoiceNo())
				.setInvoiceDate(DateUtils.formattedDate(customerInvoiceEntity.getInvoiceDate()))
				
				// Sub-total/ Total/ Balance/ Deposit/ Discounts
				.setSubTotal(new String(decoder.decode(customerInvoiceEntity.getSubTotal())))
				.setBalance(new String(decoder.decode(customerInvoiceEntity.getSubTotal())))
				.setDeposit(customerInvoiceEntity.getDeposit() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getDeposit())) : null)
				.setDiscounts(customerInvoiceEntity.getDiscounts() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getDiscounts())) : null)
				.setTotalAmount(new String(decoder.decode(customerInvoiceEntity.getTotalAmount())))
				
				// CGST/ SGST/ IGST
				.setCgstAmount(customerInvoiceEntity.getCgstAmount() != null ?
						new String(decoder.decode(customerInvoiceEntity.getCgstAmount())) : null)
				.setSgstAmount(customerInvoiceEntity.getSgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getSgstAmount())) : null)
				.setIgstAmount(customerInvoiceEntity.getIgstAmount() != null ?
						new String(decoder.decode(customerInvoiceEntity.getIgstAmount())) : null)
				.setGst4Amount(customerInvoiceEntity.getGst4Amount() != null ?
						new String(decoder.decode(customerInvoiceEntity.getGst4Amount())) : null)
				
				.setInvoiceTds(customerInvoiceEntity.getInvoiceTds() != null ?
						new String(decoder.decode(customerInvoiceEntity.getInvoiceTds())) : null)
				
				// Message on Invoice and Statement
				.setMessageInvoice(customerInvoiceEntity.getMessageInvoice())
				.setMessageStatement(customerInvoiceEntity.getMessageStatement())
				
				// Tracking no and Due Date
				.setTrackingNo(customerInvoiceEntity.getTrackingNo())
				.setDueDate(DateUtils.formattedDate(customerInvoiceEntity.getDueDate()));
		
	}
	
	
	public static CustomerInvoiceResponseDto toAllCustomerInvoiceResponseDtos(CustomerInvoiceEntity customerInvoiceEntity) {
		
		return new CustomerInvoiceResponseDto() 
				
				.setCustomerID(customerInvoiceEntity.getCustomerMasterEntity().getId())
				.setCustomerCompanyName(customerInvoiceEntity.getCustomerMasterEntity().getCompanyName())
				.setCustEmailId(customerInvoiceEntity.getCustEmailId())
				
				.setInvoiceID(customerInvoiceEntity.getId())
				.setInvoiceNo(customerInvoiceEntity.getInvoiceNo())
				.setInvoiceDate(DateUtils.formattedDate(customerInvoiceEntity.getInvoiceDate()))
				
				.setSubTotal(new String(decoder.decode(customerInvoiceEntity.getSubTotal())))
				
				.setCgstAmount(customerInvoiceEntity.getCgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getCgstAmount())) : null)
				.setSgstAmount(customerInvoiceEntity.getSgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getSgstAmount())) : null)
				.setIgstAmount(customerInvoiceEntity.getIgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getIgstAmount())) : null)
				.setGst4Amount(customerInvoiceEntity.getGst4Amount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getGst4Amount())) : null)
				
				.setBalance(customerInvoiceEntity.getBalance() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getBalance())) : null)
				.setDeposit(customerInvoiceEntity.getDeposit() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getDeposit())) : null)
				.setDiscounts(customerInvoiceEntity.getDiscounts() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getDiscounts())) : null)
				.setTotalAmount(new String(decoder.decode(customerInvoiceEntity.getTotalAmount())))
				
				.setActualTds(customerInvoiceEntity.getActualTds() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getActualTds())) : null)
				.setInvoiceTds(customerInvoiceEntity.getInvoiceTds() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getInvoiceTds())) : null)
				
				.setDueDate(DateUtils.formattedDate(customerInvoiceEntity.getDueDate()))
				.setPaymentStatus(StringNullEmpty.stringNullAndEmptyToBlank(customerInvoiceEntity.getPaymentStatus()));
	}
	
	
	public static List<CustomerInvoiceResponseDto> toAllCustomerInvoiceResponseDtosList(List<CustomerInvoiceEntity> customerInvoiceEntitiesList) {
		// TODO Auto-generated method stub
		
		List<CustomerInvoiceResponseDto> customerServiceResponseDtos = new ArrayList<CustomerInvoiceResponseDto>();
		for(CustomerInvoiceEntity customerInvoiceEntity : customerInvoiceEntitiesList) {
			customerServiceResponseDtos.add(toAllCustomerInvoiceResponseDtos(customerInvoiceEntity));			
		}
				
		return customerServiceResponseDtos;
	} 
	
	
	
	public static Set<CustomerInvoiceResponseDto> toAllCustomerInvoiceResponseDtosSet(Set<CustomerInvoiceEntity> customerInvoiceEntitiesList) {
		// TODO Auto-generated method stub
		
		Set<CustomerInvoiceResponseDto> customerServiceResponseDtos = new HashSet<CustomerInvoiceResponseDto>();
		for(CustomerInvoiceEntity customerInvoiceEntity : customerInvoiceEntitiesList) {
			customerServiceResponseDtos.add(toAllCustomerInvoiceResponseDtos(customerInvoiceEntity));			
		}
				
		return customerServiceResponseDtos;
	}
	
	public static CustomerInvoiceResponseDto toCustomerInvoiceResponseDto(CustomerInvoiceEntity customerInvoiceEntity,
			List<CustomerInvoiceServiceEntity> customerInvoiceServiceEntityList) {
		
		return new CustomerInvoiceResponseDto() 
				
				.setCustomerID(customerInvoiceEntity.getCustomerMasterEntity().getId())
				.setCustomerCompanyName(customerInvoiceEntity.getCustomerMasterEntity().getCompanyName())
				.setCustEmailId(customerInvoiceEntity.getCustEmailId())
				
				.setInvoiceID(customerInvoiceEntity.getId())
				.setInvoiceNo(customerInvoiceEntity.getInvoiceNo())
				.setInvoiceDate(DateUtils.formattedDate(customerInvoiceEntity.getInvoiceDate()))
				
				//shipping details
				.setTerms(customerInvoiceEntity.getTerms())
				.setShippingVia(customerInvoiceEntity.getShippingVia())
				.setShippingDate(DateUtils.formattedDate(customerInvoiceEntity.getShippingDate()))
				.setTrackingNo(customerInvoiceEntity.getTrackingNo())
				.setShippingAddress(customerInvoiceEntity.getShippingTo())
				.setMessageInvoice(customerInvoiceEntity.getMessageInvoice())
				.setMessageStatement(customerInvoiceEntity.getMessageStatement())
				
				.setSubTotal(new String(decoder.decode(customerInvoiceEntity.getSubTotal())))
				
				.setCgstAmount(customerInvoiceEntity.getCgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getCgstAmount())) : null)
				.setSgstAmount(customerInvoiceEntity.getSgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getSgstAmount())) : null)
				.setIgstAmount(customerInvoiceEntity.getIgstAmount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getIgstAmount())) : null)
				.setGst4Amount(customerInvoiceEntity.getGst4Amount() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getGst4Amount())) : null)
				
				.setBalance(customerInvoiceEntity.getBalance() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getBalance())) : null)
				.setDeposit(customerInvoiceEntity.getDeposit() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getDeposit())) : null)
				.setDiscounts(customerInvoiceEntity.getDiscounts() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getDiscounts())) : null)
				.setTotalAmount(new String(decoder.decode(customerInvoiceEntity.getTotalAmount())))
				
				.setActualTds(customerInvoiceEntity.getActualTds() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getActualTds())) : null)
				.setInvoiceTds(customerInvoiceEntity.getInvoiceTds() != null ? 
						new String(decoder.decode(customerInvoiceEntity.getInvoiceTds())) : null)
				
				.setDueDate(DateUtils.formattedDate(customerInvoiceEntity.getDueDate()))
				.setPaymentStatus(StringNullEmpty.stringNullAndEmptyToBlank(customerInvoiceEntity.getPaymentStatus()))
				.setCustomerInvoiceServiceResponseDtos(CustomerInvoiceServiceMapper.toCustomerInvoiceServiceResponseDtosSet(customerInvoiceServiceEntityList));	
	}
	
	public static List<CustomerInvoiceResponseDto> toCustomerInvoiceResponseDtosList(List<CustomerInvoiceEntity> customerInvoiceEntitiesList,
			List<CustomerInvoiceServiceEntity> customerInvoiceServiceEntityList) {
		// TODO Auto-generated method stub
		
		List<CustomerInvoiceResponseDto> customerServiceResponseDtos = new ArrayList<CustomerInvoiceResponseDto>();
		for(CustomerInvoiceEntity customerInvoiceEntity : customerInvoiceEntitiesList) {
			customerServiceResponseDtos.add(toCustomerInvoiceResponseDto(customerInvoiceEntity, customerInvoiceServiceEntityList));			
		}
				
		return customerServiceResponseDtos;
	} 
}
