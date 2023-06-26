package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDto storeUser(UserDto user) {
		// TODO Auto-generated method stub

		Optional<UserEntity> testUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
		if (testUser.isPresent()) {
			Error error = new Error("Email exists");
			throw new ExistingEmailException(error);
		}
		RoleEntity role = roleRepository.findByRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled((byte) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity userEntity = tempConverter.userDtoToEntity(user);
		userEntity.setRoles(roles);
		userEntity.setEnabled((byte) 1);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);

		UserDto returnValue = tempConverter.userEntityToDto(storedUser);
		return returnValue;
	}

	@Override
	public UserEntity getUserByEmail(String email) {
		// TODO Auto-generated method stub

		Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
		UserEntity returnValue = null;

		if (userEntity.isPresent()) {
			returnValue = userEntity.get();
		} else {
			Error error = new Error("Invalid User");
			throw new InvalidUserException(error);
		}

		return returnValue;
	}

	@Override
	public UserDto getCurrentUser() {
		// TODO Auto-generated method stub
		UserDto returnValue = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				String currentUserName = authentication.getName();
				UserEntity authUser = userRepository.findByEmail(currentUserName);
				returnValue = tempConverter.userEntityToDto(authUser);
			}
		} catch (Exception exc) {
			Error error = new Error("Invalid user!");
			throw new InvalidUserException(error);
		}
		return returnValue;
	}

	@Override
	public List<UserDto> listAll() {
		// TODO Auto-generated method stub
		List<UserDto> returnValue = new ArrayList<UserDto>();
		Optional<List<UserEntity>> allUsersOpt = Optional.ofNullable(userRepository.findAll());
		if (!allUsersOpt.isEmpty()) {
			allUsersOpt.get().forEach((user) -> {
				UserDto userDto = tempConverter.userEntityToDto(user);
				returnValue.add(userDto);
			});
		}
		return returnValue;
	}
}
