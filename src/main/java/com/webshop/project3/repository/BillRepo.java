package com.webshop.project3.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.webshop.project3.entity.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer>{
	

	@Query("SELECT b FROM Bill b WHERE b.createdAt >= :x")
	Page<Bill> searchByDate(@Param("x") Date s,Pageable pageable);
	
	/// Đếm số lượng đơn group by MONTH(buyDate)
	// - dùng custom object để build
	// SELECT id, MONTH(buyDate) from bill;
	// select count(*), MONTH(buyDate) from bill
	// group by MONTH(buyDate)
	@Query("SELECT count(b.id), month(b.createdAt), year(b.createdAt) "
			+ "FROM Bill b GROUP BY month(b.createdAt), year(b.createdAt) ")
	List<Object[]> thongKeBill();
}
