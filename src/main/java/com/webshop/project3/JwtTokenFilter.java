package com.webshop.project3;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.webshop.project3.service.JwtTokenService;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	JwtTokenService jwtTokenService;

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// doc header authorize
		String token = resolveToken(request);
		
		//verify token
		if(token != null) {
			// co token roi thi lay username, gọi db len user
			String username = jwtTokenService.getUsername(token);
			if(username != null) {
				Authentication auth = jwtTokenService.getAuthentication(username);
				//set vao context de co dang nhap roi
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		filterChain.doFilter(request, response);
	}

	public String resolveToken(HttpServletRequest req) {
		// check postman header
		String bearerToken = req.getHeader("Authorization");
		System.out.println(bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
