package com.webshop.project3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webshop.project3.entity.BillItem;

public interface BillItemRepo extends JpaRepository<BillItem, Integer>{

}
