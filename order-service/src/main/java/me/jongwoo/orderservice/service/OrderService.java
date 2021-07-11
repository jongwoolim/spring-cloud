package me.jongwoo.orderservice.service;


import me.jongwoo.orderservice.domain.Order;
import me.jongwoo.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    List<Order> getOrdersByUserId(String userId);
}
