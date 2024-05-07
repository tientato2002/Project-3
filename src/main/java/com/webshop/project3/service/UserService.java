package com.webshop.project3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.dto.UserDTO;
import com.webshop.project3.entity.Role;
import com.webshop.project3.entity.User;
import com.webshop.project3.repository.UserRepo;
import com.webshop.project3.schedule.JobScheduler;





@Service // tao bean: new Uservice, quan ly boi springcontain
public class UserService implements UserDetailsService{

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	JobScheduler jobScheduler;
	

	@Override
//	@Transactional // khi load user thi load luon list<role>
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepo.findByUsername(username);
		if(userEntity == null) {
			System.err.println("User_service: loadUserByName");
			throw new UsernameNotFoundException("NOT FOUND :"+username);
		}
		//convert usserEntity -> userdetails
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		//chuyen vai tro ve quyen
		for(Role role : userEntity.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return new org.springframework.security.core.userdetails.User(username,
						userEntity.getPassword(),authorities);
	}
	
	private UserDTO convert(User user) {
		return new ModelMapper().map(user, UserDTO.class);
	}

	public List<UserDTO> getAll() {
		List<User> userList = userRepo.findAll();
		
		return userList.stream().map(u -> convert(u))
				.collect(Collectors.toList());
	}

	@Transactional
	public void create(UserDTO userDTO) {
		User user = new ModelMapper().map(userDTO, User.class);
		// save entity
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepo.save(user);
	}

	@Transactional
	public void delete(int id) {
		userRepo.deleteById(id);
	}

	public UserDTO getByID(int id) {
		// Optional
		User user = userRepo.findById(id).orElse(null);
		if (user != null) {
			return convert(user);
		}
		return null;
	}

	@Transactional
	public void update(UserDTO userDTO) {
		// check
		User currentUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currentUser != null) {
			currentUser = new ModelMapper().map(userDTO, User.class);
			currentUser.setPassword(new BCryptPasswordEncoder().encode(currentUser.getPassword()));
			userRepo.save(currentUser);
		}
	}

	@Transactional
	public void updatePassword(UserDTO userDTO) {
		// check
		User currentUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currentUser != null) {
			currentUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			userRepo.save(currentUser);
		}
	}

	public PageDTO<UserDTO> search(SearchDTO searchDTO) {
		
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
		Page<User> pages = userRepo.searchByName("%"+searchDTO.getKeyword()+"%", pageable);
		
		PageDTO<UserDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(pages.getTotalPages());
		pageDTO.setTotalElements(pages.getTotalElements());
		
		List<UserDTO> roleDTOs = pages.get().map(u -> convert(u)).collect(Collectors.toList());
		
		pageDTO.setData(roleDTOs);
		return pageDTO;
	}
	
	public UserDTO findByUsername(String username) {
		User user = userRepo.findByUsername(username);
		if(user == null) {
			throw new NoResultException();
		}
		return new ModelMapper().map(user, UserDTO.class);
	}

	
	public static String generateRandomString(int length) {
        return new Random().ints(length, 97, 123)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
	
	@Transactional
	public void fogetPassword(String username) {
		User user = userRepo.findByUsername(username);
		if(user != null) {
			String newPassword = generateRandomString(6);
			user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
			jobScheduler.sendNewPassword(user.getEmail(), newPassword);
			
			userRepo.save(user);
		}
	}

}
