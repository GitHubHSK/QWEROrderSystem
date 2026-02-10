package com.qwer.qrorder.service;

import java.util.List;

import com.qwer.qrorder.domain.OrderMain;
import com.qwer.qrorder.dto.OrderRequestDTO;


public interface OrderService {

    // 注文作成
    OrderMain createOrder(OrderRequestDTO orderRequestDTO);

    // 注文詳細照会
    OrderMain getOrderById(Integer orderNo);
    
    // 全注文照会
    List<OrderMain> getAllOrders();
}