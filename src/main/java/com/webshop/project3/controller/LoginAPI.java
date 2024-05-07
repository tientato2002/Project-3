package com.webshop.project3.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.project3.dto.ResponseDTO;
import com.webshop.project3.dto.UserDTO;
import com.webshop.project3.service.JwtTokenService;
import com.webshop.project3.service.UserService;

@RestController
@RequestMapping("/")
public class LoginAPI {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenService jwtTokenService;
	
	@Autowired
	UserService userService;
	
	//get current user hien tai dang dang nhap
	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()") //method security
	public UserDTO me(Principal p) {
		String username = p.getName();
		UserDTO user = userService.findByUsername(username);
		return user;
	}
	
	
	@PostMapping("/login")
	public ResponseDTO<String> login(
			@RequestParam("username") String username,
			@RequestParam("password") String password){
		//authen : neu fail se throw exeption
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		//if login success, jwt - gen token
		return ResponseDTO.<String>builder()
				.status(200)
				.msg("login successfull!!!")
				.data(jwtTokenService.createToken(username))
				.build();
		
	}
}
