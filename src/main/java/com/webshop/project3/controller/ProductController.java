package com.webshop.project3.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.ProductDTO;
import com.webshop.project3.dto.ResponseDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.service.ProductService;

@RestController
@RequestMapping("/admin/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Value("${upload.folder}")
	private String UPLOAD_FOLDER;
	
	@PostMapping("/")
	public ResponseDTO<Void> create(@ModelAttribute @Valid ProductDTO productDTO)
	throws IllegalStateException,IOException{
		
		if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
			if (!(new File(UPLOAD_FOLDER).exists())) { //check xem file da ton tai chua
				new File(UPLOAD_FOLDER).mkdirs();  // neu chua tao file moi
			}
			String filename = productDTO.getFile().getOriginalFilename();
			
			// lay dinh dang file
			String extension = filename.substring(filename.lastIndexOf("."));
			
			// tao ten moi
			String newFilename = UUID.randomUUID().toString() + extension;
			
			File newFile = new File(UPLOAD_FOLDER + newFilename);
			productDTO.getFile().transferTo(newFile);
			
			productDTO.setImage(newFilename);// save to db
		}
		productService.create(productDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create Successfuly!!")
				.build();
	}
	
	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id){
		productService.delete(id);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("delete Successfuly!!")
				.build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute @Valid ProductDTO productDTO)
	throws IllegalStateException,IOException{
		
		if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
			if (!(new File(UPLOAD_FOLDER).exists())) { //check xem file da ton tai chua
				new File(UPLOAD_FOLDER).mkdirs();  // neu chua tao file moi
			}
			String filename = productDTO.getFile().getOriginalFilename();
			
			// lay dinh dang file
			String extension = filename.substring(filename.lastIndexOf("."));
			
			// tao ten moi
			String newFilename = UUID.randomUUID().toString() + extension;
			
			File newFile = new File(UPLOAD_FOLDER + newFilename);
			productDTO.getFile().transferTo(newFile);
			
			productDTO.setImage(newFilename);// save to db
		}
		productService.update(productDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("update Successfuly!!")
				.build();
	}
	
	@PostMapping("/search")
	public ResponseDTO<PageDTO<ProductDTO>> search(@RequestBody @Valid SearchDTO searchDTO){
		
		PageDTO<ProductDTO> pageDTO = productService.search(searchDTO);
		
		return ResponseDTO.<PageDTO<ProductDTO>>builder()
				.status(200)
				.msg("search Successfuly!!")
				.data(pageDTO)
				.build();
	}

}
