package me.jongwoo.orderservice.controller;

import lombok.RequiredArgsConstructor;
import me.jongwoo.orderservice.domain.Order;
import me.jongwoo.orderservice.dto.OrderDto;
import me.jongwoo.orderservice.messagequeue.KafkaProducer;
import me.jongwoo.orderservice.messagequeue.OrderProducer;
import me.jongwoo.orderservice.service.OrderService;
import me.jongwoo.orderservice.vo.ResponseOrder;
import me.jongwoo.orderservice.vo.ResquestOrder;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status(){
        return String.format("It`s Working in Order Service on PORT %s", env.getProperty("local.server.prot"));
    }

    // http:127.0.0.1:0/order-service/{user_id}/orders
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable String userId, @RequestBody ResquestOrder order){

        /* jpa */
        final OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa */
        final OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);

        /* kafka */
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(order.getQty() * order.getUnitPrice());


        /* send this order to the kafka */
//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);

//        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId){

        final List<Order> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(e -> {
            result.add(modelMapper.map(e, ResponseOrder.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
