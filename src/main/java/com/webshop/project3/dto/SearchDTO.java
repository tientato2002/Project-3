package com.webshop.project3.dto;

import lombok.Data;


@Data
public class SearchDTO {
//	@NotBlank(message = "{not.blank}")
	//@Size(min = 2,max = 20, message = "{size.msg}")
	private String keyword;
	
	private Integer curentPage ;
	private Integer size ;
	private String sortedField;
	
	
}
