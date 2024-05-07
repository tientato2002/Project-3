package com.webshop.project3.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class ProductColor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Color color;
	
	private int quantity;
	
	//1 product and 1 color has many image -> onetomany
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "product_images",
			joinColumns = @JoinColumn(name = "product_id"))
	@Column(name = "image")
	private List<String> images;// url
}
