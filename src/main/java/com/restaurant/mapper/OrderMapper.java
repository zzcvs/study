package com.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restaurant.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
    
    @Select("SELECT * FROM order_info WHERE order_date = #{orderDate} ORDER BY create_time DESC")
    List<OrderInfo> getOrdersByDate(@Param("orderDate") LocalDate orderDate);
    
    @Select("SELECT * FROM order_info WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<OrderInfo> getOrdersByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM order_info WHERE order_status = #{orderStatus} ORDER BY create_time DESC")
    List<OrderInfo> getOrdersByStatus(@Param("orderStatus") String orderStatus);

    @Select("SELECT * FROM order_info WHERE distribute_status = #{distributeStatus} ORDER BY create_time DESC")
    List<OrderInfo> getOrdersByDistributeStatus(@Param("distributeStatus") String distributeStatus);

    @Select("SELECT * FROM order_info WHERE order_status = 'READY' AND (distribute_status IS NULL OR distribute_status = 'PENDING') ORDER BY create_time DESC")
    List<OrderInfo> getReadyOrdersForDistribute();
}
