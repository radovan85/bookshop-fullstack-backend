package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

public class OrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer orderId;

	@NotNull
	private Integer userId;

	private List<Integer> orderedItemsIds;

	@NotNull
	private Float price;

	private String orderTimeStr;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Integer> getOrderedItemsIds() {
		return orderedItemsIds;
	}

	public void setOrderedItemsIds(List<Integer> orderedItemsIds) {
		this.orderedItemsIds = orderedItemsIds;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getOrderTimeStr() {
		return orderTimeStr;
	}

	public void setOrderTimeStr(String orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}

}
