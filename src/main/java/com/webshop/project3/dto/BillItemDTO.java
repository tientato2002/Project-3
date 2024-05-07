package com.webshop.project3.dto;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class BillItemDTO {
	
	private Integer id;

//	@JsonBackReference
	@JsonIgnoreProperties("billItems")
	private BillDTO bill;
	
	private ProductDTO product;
	
	@Min(0)
	private int quantity;
	
	@Min(0)
	private double price;
}
