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

import com.webshop.project3.dto.CategoryDTO;
import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.entity.Category;
import com.webshop.project3.repository.CategoryRepo;

public interface CategoryService {
	void create(CategoryDTO categoryDTO);
	
	void update(CategoryDTO categoryDTO);
	
	void delete(int id);
	
	PageDTO<CategoryDTO> search(SearchDTO searchDTO);
}

@Service
class CategoryServiceIpml implements CategoryService{
	
	@Autowired
	CategoryRepo categoryRepo;

	
	private CategoryDTO ConvertToDTO(Category category) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(category, CategoryDTO.class);
	}
	
	@Override
	@Transactional
	public void create(CategoryDTO categoryDTO) {
		Category category = new ModelMapper().map(categoryDTO, Category.class);
		categoryRepo.save(category);
	}

	@Override
	@Transactional
	public void update(CategoryDTO categoryDTO) {
		Category category = categoryRepo
				.findById(categoryDTO.getId())
				.orElseThrow(NoResultException::new);
		category = new ModelMapper().map(categoryDTO, Category.class);
		
		categoryRepo.save(category);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Category category = categoryRepo.findById(id)
				.orElseThrow(NoResultException::new);
		categoryRepo.delete(category);
	}

	
	@Override
	public PageDTO<CategoryDTO> search(SearchDTO searchDTO) {
		
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
		Page<Category> page = categoryRepo.searchByName("%"+searchDTO.getKeyword()+"%", pageable);
		//khoi tao
		PageDTO<CategoryDTO> pageDTO = new PageDTO<>();
		//set prop
		pageDTO.setTotalElements(page.getTotalElements());
		pageDTO.setTotalPages(page.getTotalPages());
		//data
		List<CategoryDTO> dtos = page.get().map(enti -> ConvertToDTO(enti)).collect(Collectors.toList());
		pageDTO.setData(dtos);
		
		return pageDTO;
	}
	
}
