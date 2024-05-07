package com.webshop.project3.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageDTO<T> {
	private int totalPages;
	private long totalElements;
	
	private List<T> data;
}
