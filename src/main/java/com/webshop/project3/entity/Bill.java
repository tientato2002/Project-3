package com.webshop.project3.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Bill extends TimeAuditable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String status;
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy = "bill",cascade = CascadeType.ALL,
				orphanRemoval = true)// xoa hoac sua 1,2,.. phan tu trong billItems thi trong billItem cung duoc cap nhat
									// update bang billItem thong qua bill(it dung)
	private List<BillItem> billItems;
}
