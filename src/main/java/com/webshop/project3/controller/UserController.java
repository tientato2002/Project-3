package com.webshop.project3.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.project3.dto.PageDTO;
import com.webshop.project3.dto.ResponseDTO;
import com.webshop.project3.dto.SearchDTO;
import com.webshop.project3.dto.UserDTO;
import com.webshop.project3.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@Value("${upload.folder}")
	private String UPLOAD_FOLDER;
	
	
	@PostMapping("/foget")
	public ResponseDTO<Void> fogetPassword(@RequestParam("username") String username){
		userService.fogetPassword(username);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("mat khau moi da duoc gui vao email cua ban")
				.build();
	}

	@PostMapping("/")
	public ResponseDTO<UserDTO> create(@ModelAttribute @Valid UserDTO userDTO)
			throws IllegalStateException, IOException {
		if (userDTO.getFile() != null && !userDTO.getFile().isEmpty()) {
			if (!(new File(UPLOAD_FOLDER).exists())) {
				new File(UPLOAD_FOLDER).mkdirs();
			}
			String filename = userDTO.getFile().getOriginalFilename();
			// lay dinh dang file
			String extension = filename.substring(filename.lastIndexOf("."));
			// tao ten moi
			String newFilename = UUID.randomUUID().toString() + extension;

			File newFile = new File(UPLOAD_FOLDER + newFilename);

			userDTO.getFile().transferTo(newFile);

			userDTO.setAvatar(newFilename);// save to db
		}

		userService.create(userDTO);
		return ResponseDTO.<UserDTO>builder().status(200).data(userDTO).build();
	}

	/// /user/download/abc.jpg
	@GetMapping("/download/{filename}")
	public void download(@PathVariable("filename") String filename, HttpServletResponse response) 
			throws IOException {
		File file = new File(UPLOAD_FOLDER + filename);
		Files.copy(file.toPath(), response.getOutputStream());
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		userService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("delete successfully!").build();

	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute @Valid UserDTO userDTO) 
			throws IllegalStateException, IOException {
		if (userDTO.getFile() != null && !userDTO.getFile().isEmpty()) {
			String filename = userDTO.getFile().getOriginalFilename();
			// lay dinh dang file
			String extension = filename.substring(filename.lastIndexOf("."));
			// tao ten moi
			String newFilename = UUID.randomUUID().toString() + extension;

			File newFile = new File(UPLOAD_FOLDER + newFilename);

			userDTO.getFile().transferTo(newFile);

			userDTO.setAvatar(newFilename);// save to db
		}
		userService.update(userDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("update Successfully!")
				.build();
	}

	@GetMapping("/")
	public ResponseDTO<UserDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<UserDTO>builder()
				.msg("user ID:" + id)
				.status(200)
				.data(userService.getByID(id))
				.build();
	}

	@PutMapping("/password")
	public ResponseDTO<Void> updatePassword(@RequestBody @Valid UserDTO u) {
		userService.updatePassword(u);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("update password successfuly!")
				.build();
	}
	
	@PostMapping("/search")
	public ResponseDTO<PageDTO<UserDTO>> search(@RequestBody @Valid SearchDTO searchDTO){
		
		return ResponseDTO.<PageDTO<UserDTO>>builder()
				.status(200)
				.msg("ds user")
				.data(userService.search(searchDTO))
				.build();
	}

}
