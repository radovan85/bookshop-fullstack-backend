package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;

@Component
public class TempConverter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OrderItemRepository itemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ModelMapper mapper;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private DecimalFormat decfor = new DecimalFormat("0.00");

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> rolesOpt = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOpt.isEmpty()) {
			rolesOpt.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOpt = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOpt.isEmpty()) {
			rolesIdsOpt.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> usersOpt = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();
		if (!usersOpt.isEmpty()) {
			usersOpt.get().forEach((user) -> {
				userIds.add(user.getId());
			});
		}

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIdsOpt = Optional.ofNullable(roleDto.getUsersIds());
		List<UserEntity> users = new ArrayList<>();
		if (usersIdsOpt.isPresent()) {
			usersIdsOpt.get().forEach((userId) -> {
				UserEntity userEntity = userRepository.findById(userId).get();
				users.add(userEntity);
			});
		}

		returnValue.setUsers(users);
		return returnValue;
	}

	public BookDto bookEntityToDto(BookEntity book) {
		BookDto returnValue = mapper.map(book, BookDto.class);
		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public BookEntity bookDtoToEntity(BookDto book) {
		BookEntity returnValue = mapper.map(book, BookEntity.class);
		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity order) {
		OrderDto returnValue = mapper.map(order, OrderDto.class);
		Optional<UserEntity> userOpt = Optional.ofNullable(order.getUser());
		if (userOpt.isPresent()) {
			returnValue.setUserId(userOpt.get().getId());
		}

		List<Integer> orderItemsIds = new ArrayList<Integer>();
		Optional<List<OrderItemEntity>> orderedItemsOpt = Optional.ofNullable(order.getOrderedItems());
		if (!orderedItemsOpt.isEmpty()) {
			orderedItemsOpt.get().forEach((item) -> {
				orderItemsIds.add(item.getOrderItemId());
			});
		}

		returnValue.setOrderedItemsIds(orderItemsIds);

		Optional<Timestamp> orderTimeOpt = Optional.ofNullable(order.getOrderTime());
		if (orderTimeOpt.isPresent()) {
			LocalDateTime orderTimeLocal = orderTimeOpt.get().toLocalDateTime();
			String orderTimeStr = orderTimeLocal.format(formatter);
			returnValue.setOrderTimeStr(orderTimeStr);
		}

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);
		Optional<Integer> userIdOpt = Optional.ofNullable(order.getUserId());
		if (userIdOpt.isPresent()) {
			Integer userId = userIdOpt.get();
			UserEntity userEntity = userRepository.findById(userId).get();
			returnValue.setUser(userEntity);
		}

		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		Optional<List<Integer>> orderedItemsIdsOpt = Optional.ofNullable(order.getOrderedItemsIds());
		if (!orderedItemsIdsOpt.isEmpty()) {
			orderedItemsIdsOpt.get().forEach((itemId) -> {
				OrderItemEntity itemEntity = itemRepository.findById(itemId).get();
				orderedItems.add(itemEntity);
			});
		}

		returnValue.setOrderedItems(orderedItems);

		Optional<String> orderTimeStrOpt = Optional.ofNullable(order.getOrderTimeStr());
		if (orderedItemsIdsOpt.isPresent()) {
			String orderTimeStr = orderTimeStrOpt.get();
			LocalDateTime orderTimeLocal = LocalDateTime.parse(orderTimeStr);
			Timestamp orderTime = Timestamp.valueOf(orderTimeLocal);
			returnValue.setOrderTime(orderTime);
		}

		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity item) {
		OrderItemDto returnValue = mapper.map(item, OrderItemDto.class);
		Optional<OrderEntity> orderOpt = Optional.ofNullable(item.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto item) {
		OrderItemEntity returnValue = mapper.map(item, OrderItemEntity.class);
		Optional<Integer> orderIdOpt = Optional.ofNullable(item.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

}
