package com.webshop.project3.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.project3.dto.BillDTO;
import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.ResponseDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.dto.UserDTO;
import com.webshop.project3.schedule.JobScheduler;
import com.webshop.project3.service.BillService;
import com.webshop.project3.service.UserService;

@RestController
public class BillController {

	@Autowired
	BillService billService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JobScheduler jobScheduler;

	
	
	
	//admin tao don
	@PostMapping("/admin/bill")
	public ResponseDTO<BillDTO> add(@RequestBody @Valid BillDTO billDTO) {
		billService.create(billDTO);
		return ResponseDTO.<BillDTO>builder().status(200).data(billDTO).build();
		
	}
	
	
	//customer tao don
	@PostMapping("/customer/bill")
	public ResponseDTO<BillDTO> add(@RequestBody @Valid BillDTO billDTO,
			Principal p) {
		SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = p.getName();
		UserDTO user = userService.findByUsername(username);
		billDTO.setUser(user);
		
		billService.create(billDTO);
		return ResponseDTO.<BillDTO>builder().status(200).data(billDTO).build();
	}
	
	@DeleteMapping("/bill/{id}") // /1
	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
		billService.delete(id);
		return ResponseDTO.<Void>builder().status(200).build();
	}
	
	@PostMapping("/bill/search")
	public ResponseDTO<PageDTO<BillDTO>> search(
			@RequestBody @Valid SearchDTO searchDTO) {
		return ResponseDTO.<PageDTO<BillDTO>>builder()
				.status(200)
				.data(billService.search(searchDTO))
				.build();
	}
	
	@PutMapping("/bill")
	public ResponseDTO<Void> update(@RequestBody @Valid BillDTO bill) {
		billService.update(bill);
		return ResponseDTO.<Void>builder().status(200).build();
	}
	
	@GetMapping("/bill/tk")
	public ResponseDTO<List<String>> tk(){
		return ResponseDTO.<List<String>>builder()
				.status(200)
				.msg("thong ke theo thang:")
				.data(billService.thongkeThang())
				.build();
	}

}
