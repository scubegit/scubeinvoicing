package com.scube.invoicing.dto.mapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.scube.invoicing.dto.InvoiceCreditNoteResponseDto;
import com.scube.invoicing.entity.CustomerCreditNoteEntity;
import com.scube.invoicing.entity.CustomerInvoiceEntity;
import com.scube.invoicing.util.DateUtils;
import com.scube.invoicing.util.StringNullEmpty;

public class InvoiceCreditNoteResponseMapper {
	
	static Base64.Decoder decoder = Base64.getDecoder();
	
	public static InvoiceCreditNoteResponseDto toInvoiceResponseDto(CustomerInvoiceEntity customerInvoiceEntity) {
		return new InvoiceCreditNoteResponseDto()
				// Customer Invoice ID, Customer ID, Customer Details
				.setCustomerInvoiceID(customerInvoiceEntity.getId())
				.setCustomerID(customerInvoiceEntity.getCustomerMasterEntity().getId())
				.setCustomerCompanyName(customerInvoiceEntity.getCustomerMasterEntity().getCompanyName())
				
				// Invoice No and Invoice Date
				.setCustomerInvoiceNo(customerInvoiceEntity.getInvoiceNo())
				.setInvoiceDate(DateUtils.formatDateToDDMMYYYYFormat(customerInvoiceEntity.getInvoiceDate()))
				
				// Invoice Payment Status and Invoice Amount
				.setCustomerInvoiceAmount(customerInvoiceEntity.getTotalAmount() != null ? StringNullEmpty.stringNullAndEmptyToBlank(new String(decoder.decode(customerInvoiceEntity.getTotalAmount()))): "")
				.setCurrency(customerInvoiceEntity.getCustomerMasterEntity().getCurrencyMasterEntity().getCurrencyName())
				.setAmountPendingFlag(customerInvoiceEntity.getPaymentStatus())
				
				//Cgst, Sgst, Igst, subtotal, TotalNumber
				.setCgstamount(customerInvoiceEntity.getCgstAmount() != null ? StringNullEmpty.stringNullAndEmptyToBlank(new String(decoder.decode(customerInvoiceEntity.getCgstAmount()))):"")
				.setSgstamount(customerInvoiceEntity.getSgstAmount()!= null ? StringNullEmpty.stringNullAndEmptyToBlank(new String(decoder.decode(customerInvoiceEntity.getSgstAmount()))):"")
				.setIgstamount(customerInvoiceEntity.getIgstAmount() != null ? StringNullEmpty.stringNullAndEmptyToBlank(new String(decoder.decode(customerInvoiceEntity.getIgstAmount()))):"")
				
				.setGstnumber(customerInvoiceEntity.getCustomerMasterEntity().getGstin() != null ? StringNullEmpty.stringNullAndEmptyToBlank(customerInvoiceEntity.getCustomerMasterEntity().getGstin()): "")
				.setSubtotal(customerInvoiceEntity.getSubTotal() !=null ? StringNullEmpty.stringNullAndEmptyToBlank(new String(decoder.decode(customerInvoiceEntity.getSubTotal()))):"");
			//	.setTotalamount(new String(decoder.decode(customerInvoiceEntity.getTotalAmount())));
	}
	
	public static List<InvoiceCreditNoteResponseDto> toInvoiceResponseDtosList(List<CustomerInvoiceEntity> customerInvoiceEntitiesList) {
		// TODO Auto-generated method stub
		
		List<InvoiceCreditNoteResponseDto> invoiceCreditNoteResponseDtos = new ArrayList<InvoiceCreditNoteResponseDto>();
		for(CustomerInvoiceEntity customerInvoiceEntity : customerInvoiceEntitiesList) {
			invoiceCreditNoteResponseDtos.add(toInvoiceResponseDto(customerInvoiceEntity));	
		}
		
		return invoiceCreditNoteResponseDtos;
	}
	
	
	public static InvoiceCreditNoteResponseDto toCreditNoteResponseDto(CustomerCreditNoteEntity creditNoteEntity) {
		return new InvoiceCreditNoteResponseDto()
				// Customer ID and Customer Details
				.setCustomerID(creditNoteEntity.getCustomerMasterEntity().getId())
				.setCustomerCompanyName(creditNoteEntity.getCustomerMasterEntity().getCompanyName())
				
				// Customer Credit Note No and Credit Note Date
				.setCustomerCreditNoteNo(creditNoteEntity.getCreditNoteNo())
				.setCreditNoteDate(DateUtils.formatDateToDDMMYYYYFormat(creditNoteEntity.getCreditNoteDate()))
				
				// Customer Credits Remaining
				.setCustomerCreditAmount(new String(decoder.decode(creditNoteEntity.getTotalAmount())));
				//.setCustomerCreditsRemaining(new String(decoder.decode(creditNoteEntity.getCreditsRemaining())));
				
	}
	
	public static List<InvoiceCreditNoteResponseDto> toCreditNoteResponseDtosList(List<CustomerCreditNoteEntity> creditNoteEntitiesList) {
		// TODO Auto-generated method stub
		
		List<InvoiceCreditNoteResponseDto> invoiceCreditNoteResponseDtos = new ArrayList<InvoiceCreditNoteResponseDto>();
		for(CustomerCreditNoteEntity creditNoteEntity : creditNoteEntitiesList) {
			invoiceCreditNoteResponseDtos.add(toCreditNoteResponseDto(creditNoteEntity));			
		}
		
		return invoiceCreditNoteResponseDtos;
	} 

}
