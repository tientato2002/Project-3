package com.webshop.project3.controller;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.webshop.project3.dto.ResponseDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

	@ExceptionHandler({ NoResultException.class, EmptyResultDataAccessException.class })
	public ResponseDTO<String> notFound(Exception e) {
		log.info("INFO", e);
		// cach1:
//		return ResponseDTO.<String>builder()
//				.status(404).msg("No Data").build();
		// cach2:
//		ResponseDTO<String> responseDTO = new ResponseDTO<>();
//		responseDTO.setStatus(404);
//		responseDTO.setMsg("No Data");
		// cach3:
		ResponseDTO<String> responseDTO = new ResponseDTO<>(400, "No Data");
		return responseDTO;
	}

	@ExceptionHandler({ BindException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // HTTP Status code(ko co mac dinh la 200)
	public ResponseDTO<String> badRequest(BindException e) {
		log.info("bad request");

		List<ObjectError> errors = e.getBindingResult().getAllErrors();
		String msg = "";
		for (ObjectError err : errors) {
			FieldError fieldError = (FieldError) err;

			msg += fieldError.getField() + ":" + err.getDefaultMessage() + ";";
		}
		return ResponseDTO.<String>builder().status(400).msg(msg).build();
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ResponseDTO<Void> conflict(Exception ex) {
		log.info("ex: ", ex);
		return ResponseDTO.<Void>builder().status(409).msg("CONFLICT").build();// view
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseDTO<String> accessBeny(Exception e){
		log.info("INFO:::",e);
		return ResponseDTO.<String>builder()
				.status(403).msg("Acess Deny").build();
	}
	
	@ExceptionHandler({ ExpiredJwtException.class, MalformedJwtException.class, BadCredentialsException.class })
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ResponseDTO<Void> unauthorized(Exception ex) {
		return ResponseDTO.<Void>builder().status(401).msg("JWT Expired").build();// view
	}
	
	@ExceptionHandler({ Exception.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseDTO<Void> exception(Exception ex) {
		log.error("ex: ", ex);
		return ResponseDTO.<Void>builder().status(500).msg("SERVER ERROR").build();// view
	}
}
