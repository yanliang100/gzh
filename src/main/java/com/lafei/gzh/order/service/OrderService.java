package com.lafei.gzh.order.service;

import com.lafei.gzh.order.bean.Order;
import com.lafei.gzh.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;

@Service("orderService")
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Transactional
    public Order birthNewOrderSn(BigDecimal orderPrice) throws Exception {
        String orderSn=orderMapper.selectMaxOrderSn("");
        if(orderSn!=null && !orderSn.isEmpty()){
            orderSn=String.valueOf(Integer.parseInt(orderSn)+1);
        }else{
            orderSn="1000000000";
        }

        Order newOrder=new Order();
        newOrder.setOrderSn(orderSn);
        newOrder.setOrderPrice(orderPrice);
        orderMapper.insert(newOrder);
        return newOrder;
    }

    public Order queryOrderByOrderSn(String orderSn) throws Exception {
        return orderMapper.selectOrderByOrderSn(orderSn);
    }

    public int update(Order order) throws Exception {
        return orderMapper.update(order);
    }
}
