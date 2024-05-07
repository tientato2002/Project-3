package com.webshop.project3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.webshop.project3.dto.BillDTO;
import com.webshop.project3.dto.BillItemDTO;
import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.entity.Bill;
import com.webshop.project3.entity.BillItem;
import com.webshop.project3.entity.User;
import com.webshop.project3.repository.BillItemRepo;
import com.webshop.project3.repository.BillRepo;
import com.webshop.project3.repository.ProductRepo;
import com.webshop.project3.repository.UserRepo;
import com.webshop.project3.schedule.JobScheduler;

public interface BillService {
	void create(BillDTO billDTO);
	
	void update(BillDTO billDTO);
	
	void delete(int id);
	
	PageDTO<BillDTO> search(SearchDTO searchDTO);
	
	BillDTO getById(int id);
	
	List<String> thongkeThang();
}

@Service
class BillServiceImpl implements BillService {
	
	@Autowired
	BillRepo billRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BillItemRepo billItemRepo;

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	JobScheduler jobScheduler;
	
	
	private BillDTO ConvertToDTO(Bill bill) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(bill, BillDTO.class);
	}
	
	@Override
	@Transactional
	public void create(BillDTO billDTO) {
		User user = userRepo.findById(billDTO.getUser().getId()).orElseThrow(NoResultException::new);
		
		Bill bill = new Bill();
		bill.setUser(user);
		bill.setStatus(billDTO.getStatus());
		List<BillItem> billItems = new ArrayList<>();
		
		for(BillItemDTO billItemDTO : billDTO.getBillItems()) {
			BillItem billItem = new BillItem();
			billItem.setBill(bill);
			billItem.setProduct(
					productRepo.findById(billItemDTO.getProduct().getId()).orElseThrow(NoResultException::new));
			billItem.setPrice(billItemDTO.getPrice());
			billItem.setQuantity(billItemDTO.getQuantity());
			
			billItems.add(billItem);
		}
		
		bill.setBillItems(billItems);
		String email = user.getEmail();
		
		//
		jobScheduler.sendAdminEmail(email);
		//
		billRepo.save(bill);
	}

	@Override
	@Transactional
	public void update(BillDTO billDTO) {
		Bill bill = billRepo.findById(billDTO.getId()).orElseThrow(NoResultException::new);
		User user = userRepo.findById(billDTO.getUser().getId()).orElseThrow(NoResultException::new);
		
		bill.setUser(user);
		bill.setStatus(billDTO.getStatus());
		
		billRepo.save(bill);
		
	}

	@Override
	@Transactional
	public void delete(int id) {
		Bill bill = billRepo.findById(id).orElseThrow(NoResultException::new);
		billRepo.save(bill);
		
	}

	@Override
	public PageDTO<BillDTO> search(SearchDTO searchDTO) {
		if (searchDTO.getCurentPage() == null) {
			searchDTO.setCurentPage(0);
		}
		if (searchDTO.getSize() == null) {
			searchDTO.setSize(5);
		}
		if (searchDTO.getKeyword() == null) {
			searchDTO.setKeyword("");
		}
		
		
		Pageable pageable = PageRequest.of(searchDTO.getCurentPage(), searchDTO.getSize());
		Page<Bill> pages = billRepo.findAll(pageable);
		
		PageDTO<BillDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(pages.getTotalPages());
		pageDTO.setTotalElements(pages.getTotalElements());
		
		List<BillDTO> roleDTOs = pages.get().map(b -> ConvertToDTO(b)).collect(Collectors.toList());
		
		pageDTO.setData(roleDTOs);
		return pageDTO;
	}

	@Override
	public BillDTO getById(int id) {
		Bill bill = billRepo.findById(id).orElseThrow(NoResultException::new);
		return new ModelMapper().map(bill, BillDTO.class);
	}

	@Override
	public List<String> thongkeThang() {
		
		jobScheduler.createFileExcel();
		
		jobScheduler.sendMailExcel("4269manh@gmail.com");
		
		List<String> res = new ArrayList<>();
		List<Object[]> kq = billRepo.thongKeBill();
		for(Object[] obj : kq) {
			String detail = "so don thang "+
		String.valueOf(obj[1])+" nam:"+String.valueOf(obj[2])+" la:"+
					String.valueOf(obj[0])+" don.";
			res.add(detail);
		}
		return res;
	}
	
}