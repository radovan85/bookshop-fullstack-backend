package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InstanceNotExistException;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private OrderItemRepository itemRepository;

	@Override
	public List<OrderDto> listAllByUserId(Integer userId) {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		Optional<List<OrderEntity>> allOrdersOpt = Optional.ofNullable(orderRepository.findAllByUserId(userId));
		if (!allOrdersOpt.isEmpty()) {
			allOrdersOpt.get().forEach((order) -> {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			});
		}
		return returnValue;
	}

	@Override
	public OrderDto addOrder(Integer bookId) {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
		if (bookOpt.isPresent()) {
			BookEntity bookEntity = bookOpt.get();
			OrderEntity order = new OrderEntity();
			order.setPrice(bookEntity.getPrice());
			order.setUser(authUser);

			OrderItemEntity itemEntity = new OrderItemEntity();
			itemEntity.setBookTitle(bookEntity.getTitle());
			itemEntity.setPrice(bookEntity.getPrice());
			orderedItems.add(itemEntity);

			order.setOrderedItems(orderedItems);
			OrderEntity storedOrder = orderRepository.save(order);

			for (OrderItemEntity item : orderedItems) {
				item.setOrder(storedOrder);
				itemRepository.saveAndFlush(item);
			}

			returnValue = tempConverter.orderEntityToDto(storedOrder);
		} else {
			Error error = new Error("Book not found!");
			throw new InstanceNotExistException(error);
		}
		return returnValue;
	}

	@Override
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		Optional<List<OrderEntity>> allOrdersOpt = Optional.ofNullable(orderRepository.findAll());
		if (!allOrdersOpt.isEmpty()) {
			allOrdersOpt.get().forEach((order) -> {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			});
		}
		return returnValue;
	}

	@Override
	public OrderDto getOrderById(Integer orderId) {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isPresent()) {
			returnValue = tempConverter.orderEntityToDto(orderOpt.get());
		} else {
			Error error = new Error("Order not found!");
			throw new InstanceNotExistException(error);
		}
		return returnValue;
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
		if(orderOpt.isPresent()) {
			orderRepository.deleteById(orderId);
			orderRepository.flush();
		}else {
			Error error = new Error("Order not found!");
			throw new InstanceNotExistException(error);
		}
	}

}
