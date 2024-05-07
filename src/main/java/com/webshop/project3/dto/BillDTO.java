package com.webshop.project3.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BillDTO {
	private Integer id;
	private String status;	
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;
	
	private UserDTO user;
	
//	@JsonManagedReference
	private List<BillItemDTO> billItems;
	
}
