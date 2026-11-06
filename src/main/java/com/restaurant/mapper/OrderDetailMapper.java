package com.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    
    @Select("SELECT * FROM order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> getOrderDetailsByOrderId(@Param("orderId") Long orderId);
}
