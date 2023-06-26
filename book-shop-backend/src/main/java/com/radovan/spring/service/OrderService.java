package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderDto;

public interface OrderService {

	List<OrderDto> listAllByUserId(Integer userId);
	
	List<OrderDto> listAll();
	
	OrderDto addOrder(Integer bookId);
	
	OrderDto getOrderById(Integer orderId);
	
	void deleteOrder(Integer orderId);
}
