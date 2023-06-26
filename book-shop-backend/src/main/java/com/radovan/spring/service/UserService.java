package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;

public interface UserService {

	UserDto storeUser(UserDto user);
	
	UserEntity getUserByEmail(String email);
	
	UserDto getCurrentUser();
	
	List<UserDto> listAll();
}
