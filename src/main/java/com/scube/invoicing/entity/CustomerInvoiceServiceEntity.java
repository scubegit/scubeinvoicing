package com.scube.invoicing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer_invoice_service")
@Getter @Setter
public class CustomerInvoiceServiceEntity extends BaseEntity {
	
	@OneToOne
	@JoinColumn(name = "fk_customer")
	private CustomerMasterEntity customerMasterEntity;
	
	@OneToOne
	@JoinColumn(name = "fk_customer_invoice")
	private CustomerInvoiceEntity customerInvoiceEntity;

	@OneToOne
	@JoinColumn(name = "fk_service")
	private ServiceMasterEntity serviceMasterEntity;
	
	@Column(name = "hsn_sac")
	private String hsnorSac;
	
	@Column(name = "sku")
	private String sku;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "quantity")
	private String quantity;
	
	@Column(name = "rate")
	private double rate;
	
	@Column(name = "amount")
	private String amount;
	
	@Column(name = "service_amount_with_gst")
	private String serviceAmountWithGst;
	
	@Column(name = "gst_amount")
	private String gstAmount;
	
	@OneToOne
	@JoinColumn(name = "fk_tax")
	private GSTMasterEntity gstMasterEntity;
	
}
