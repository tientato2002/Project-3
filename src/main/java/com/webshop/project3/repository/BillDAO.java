package com.webshop.project3.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.webshop.project3.entity.Bill;

@Repository
public class BillDAO {
	@PersistenceContext
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Bill> searchByDate(Date s){
		String jpql = "SELECT b FROM Bill b WHERE b.createdAt >= :x ";
		
		return entityManager.createQuery(jpql)
				.setParameter("x", s)
				.setMaxResults(10)
				.setFirstResult(0)
				.getResultList();
	}

}
