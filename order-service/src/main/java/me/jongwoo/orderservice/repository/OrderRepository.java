package me.jongwoo.orderservice.repository;

import me.jongwoo.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(String orderId);
    List<Order> findByUserId(String userId);
}
