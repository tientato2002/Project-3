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
import com.webshop.project3.dto.RoleDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.entity.Role;
import com.webshop.project3.repository.RoleRepo;

public interface RoleService {
	void create(RoleDTO roleDTO);
	
	void update(RoleDTO roleDTO);
	
	void delete(int id);
	
	PageDTO<RoleDTO> search(SearchDTO searchDTO);
	
}


@Service
class RoleServiceImpl implements RoleService {
	
	@Autowired
	RoleRepo roleRepo;

	
	@Override
	@Transactional
	public void create(RoleDTO roleDTO) {
		
		Role role = new ModelMapper().map(roleDTO, Role.class);
		roleRepo.save(role);
		
//		roleDTO.setId(role.getId());
	}

	
	@Override
	@Transactional
	public void update(RoleDTO roleDTO) {
		Role role = roleRepo.findById(roleDTO.getId()).orElseThrow(NoResultException::new);
		
		role.setName(roleDTO.getName());
		roleRepo.save(role);
	}

	@Override
	@Transactional
	public void delete(int id) {
		roleRepo.findById(id).orElseThrow(NoResultException::new);		
		
		roleRepo.deleteById(id);
	}

	@Override
	public PageDTO<RoleDTO> search(SearchDTO searchDTO) {
		
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
		Page<Role> pages = roleRepo.searchByName("%"+searchDTO.getKeyword()+"%", pageable);
		
		PageDTO<RoleDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(pages.getTotalPages());
		pageDTO.setTotalElements(pages.getTotalElements());
		
		List<RoleDTO> roleDTOs = pages.get().map(role -> convertToDto(role)).collect(Collectors.toList());
		
		pageDTO.setData(roleDTOs);
		return pageDTO;
	}
	
	private RoleDTO convertToDto(Role role) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(role, RoleDTO.class);
	}
	
}