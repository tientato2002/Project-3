package com.webshop.project3.service;

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

import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.ProductDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.entity.Product;
import com.webshop.project3.repository.ProductRepo;

public interface ProductService {
	void create(ProductDTO productDTO);
	
	void update(ProductDTO productDTO);
	
	void delete(int id);
	
	PageDTO<ProductDTO> search(SearchDTO searchDTO);
}

@Service
class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductRepo productRepo;


	private ProductDTO ConvertToDTO(Product product) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(product, ProductDTO.class);
	}
	
	
	@Override
	@Transactional
	public void create(ProductDTO productDTO) {
		Product product = new ModelMapper().map(productDTO, Product.class);
		productRepo.save(product);
	}

	@Override
	@Transactional
	public void update(ProductDTO productDTO) {
		Product product = productRepo.findById(productDTO.getId()).orElseThrow(NoResultException::new);
		product = new ModelMapper().map(productDTO, Product.class);
		productRepo.save(product);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Product product = productRepo.findById(id).orElseThrow(NoResultException::new);
		productRepo.delete(product);
	}

	@Override
	public PageDTO<ProductDTO> search(SearchDTO searchDTO) {
		if(searchDTO.getKeyword() == null) {
			searchDTO.setKeyword("");
		}
		if(searchDTO.getCurentPage() == null) {
			searchDTO.setCurentPage(0);
		}
		if(searchDTO.getSize() == null) {
			searchDTO.setSize(10);
		}
		
		Pageable pageable = PageRequest.of(searchDTO.getCurentPage(), searchDTO.getSize());
		Page<Product> page = productRepo.searchByName("%"+searchDTO.getKeyword()+"%", pageable);
		//khoi tao
		PageDTO<ProductDTO> pageDTO = new PageDTO<>();
		//set prop
		pageDTO.setTotalElements(page.getTotalElements());
		pageDTO.setTotalPages(page.getTotalPages());
		//data
		List<ProductDTO> dtos = page.get().map(enti -> ConvertToDTO(enti)).collect(Collectors.toList());
		pageDTO.setData(dtos);
		
		return pageDTO;
	}
	
}