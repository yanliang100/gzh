package com.lafei.gzh.order.mapper;

import com.lafei.gzh.order.bean.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    String selectMaxOrderSn(String orderSnPrefix) throws Exception;
    int insert(Order newOrder) throws Exception;
    int update(Order order) throws Exception;
    Order selectOrderByOrderSn(String orderSn) throws Exception;
}
