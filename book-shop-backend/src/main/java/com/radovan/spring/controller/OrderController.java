package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@GetMapping(value = "/allOrders")
	public ResponseEntity<List<OrderDto>> getAllOrders() {
		List<OrderDto> allOrders = orderService.listAll();
		return ResponseEntity.ok().body(allOrders);
	}

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@GetMapping(value = "/allOrders/{userId}")
	public ResponseEntity<List<OrderDto>> listAllByUserId(@PathVariable("userId") Integer userId) {
		List<OrderDto> allOrders = orderService.listAllByUserId(userId);
		return ResponseEntity.ok().body(allOrders);
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@PostMapping(value = "/addOrder/{bookId}")
	public ResponseEntity<String> addOrder(@PathVariable("bookId") Integer bookId) {

		orderService.addOrder(bookId);
		return ResponseEntity.ok().body("Order completed!");
	}

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@GetMapping(value = "/orderDetails/{orderId}")
	public ResponseEntity<OrderDto> getOrderDetails(@PathVariable("orderId") Integer orderId) {
		OrderDto order = orderService.getOrderById(orderId);
		return ResponseEntity.ok().body(order);
	}

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@DeleteMapping(value = "/deleteOrder/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable("orderId") Integer orderId) {
		orderService.deleteOrder(orderId);
		return ResponseEntity.ok().body("Order with id " + orderId + " is permanently deleted!");
	}

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@GetMapping(value = "/allItems/{orderId}")
	public ResponseEntity<List<OrderItemDto>> getAllItems(@PathVariable("orderId") Integer orderId) {
		List<OrderItemDto> allItems = orderItemService.listAllByOrderId(orderId);
		return ResponseEntity.ok().body(allItems);
	}

}
