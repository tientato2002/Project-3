package com.webshop.project3.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.ResponseDTO;
import com.webshop.project3.dto.RoleDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.service.RoleService;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody @Valid RoleDTO roleDTO){
		roleService.create(roleDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create successful!!!")
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable("id") int id){
		roleService.delete(id);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("delete successful!!!")
				.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<RoleDTO> edit(@RequestBody @Valid RoleDTO roleDTO){
		roleService.update(roleDTO);
		
		return ResponseDTO.<RoleDTO>builder()
				.status(200)
				.msg("update successfull!!!")
				.data(roleDTO)
				.build();
	}
	
	@PostMapping("/search")
	public ResponseDTO<PageDTO<RoleDTO>> search(@RequestBody @Valid SearchDTO searchDTO){
		PageDTO<RoleDTO> pageDTO = roleService.search(searchDTO);
		
		return ResponseDTO.<PageDTO<RoleDTO>>builder()
				.status(200)
				.data(pageDTO)
				.msg("search successfull!!!")
				.build();
	}
}
