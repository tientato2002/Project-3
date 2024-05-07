
package com.webshop.project3.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.project3.dto.CategoryDTO;
import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.ResponseDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.service.CategoryService;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/")
	public ResponseDTO<Void> create(@RequestBody @Valid CategoryDTO categoryDTO){
		categoryService.create(categoryDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create Successfuly!!")
				.build();
	}
	
	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id){
		categoryService.delete(id);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("delete Successfuly!!")
				.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody @Valid CategoryDTO categoryDTO){
		categoryService.update(categoryDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("update Successfuly!!")
				.build();
	}
	
	@PostMapping("/search")
	public ResponseDTO<PageDTO<CategoryDTO>> search(@RequestBody @Valid SearchDTO searchDTO){
		
		PageDTO<CategoryDTO> pageDTO = categoryService.search(searchDTO);
		
		return ResponseDTO.<PageDTO<CategoryDTO>>builder()
				.status(200)
				.msg("search Successfuly!!")
				.data(pageDTO)
				.build();
	}
}
