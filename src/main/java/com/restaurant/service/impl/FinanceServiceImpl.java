package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.FinanceRecord;
import com.restaurant.entity.OrderInfo;
import com.restaurant.mapper.FinanceRecordMapper;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceServiceImpl extends ServiceImpl<FinanceRecordMapper, FinanceRecord> implements FinanceService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public List<FinanceRecord> getFinanceRecordsByDate(LocalDate date) {
        QueryWrapper<FinanceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_date", date);
        return list(queryWrapper);
    }
    
    @Override
    public Map<String, Object> getDailyStatistics(LocalDate date) {
        Map<String, Object> statistics = new HashMap<>();
        
        List<OrderInfo> orders = orderMapper.getOrdersByDate(date);
        
        int totalOrders = orders.size();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal unpaidAmount = BigDecimal.ZERO;
        
        for (OrderInfo order : orders) {
            totalAmount = totalAmount.add(order.getTotalAmount());
            if ("PAID".equals(order.getPaymentStatus())) {
                paidAmount = paidAmount.add(order.getTotalAmount());
            } else {
                unpaidAmount = unpaidAmount.add(order.getTotalAmount());
            }
        }
        
        statistics.put("date", date);
        statistics.put("totalOrders", totalOrders);
        statistics.put("totalAmount", totalAmount);
        statistics.put("paidAmount", paidAmount);
        statistics.put("unpaidAmount", unpaidAmount);
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> statistics = new HashMap<>();
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("order_date", startDate, endDate);
        List<OrderInfo> orders = orderMapper.selectList(queryWrapper);
        
        int totalOrders = orders.size();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal unpaidAmount = BigDecimal.ZERO;
        
        for (OrderInfo order : orders) {
            totalAmount = totalAmount.add(order.getTotalAmount());
            if ("PAID".equals(order.getPaymentStatus())) {
                paidAmount = paidAmount.add(order.getTotalAmount());
            } else {
                unpaidAmount = unpaidAmount.add(order.getTotalAmount());
            }
        }
        
        statistics.put("year", year);
        statistics.put("month", month);
        statistics.put("totalOrders", totalOrders);
        statistics.put("totalAmount", totalAmount);
        statistics.put("paidAmount", paidAmount);
        statistics.put("unpaidAmount", unpaidAmount);
        
        return statistics;
    }
    
    @Override
    public boolean generateDailyFinanceRecord(LocalDate date) {
        Map<String, Object> statistics = getDailyStatistics(date);
        
        FinanceRecord record = new FinanceRecord();
        record.setRecordDate(date);
        record.setOrderCount((Integer) statistics.get("totalOrders"));
        record.setTotalAmount((BigDecimal) statistics.get("totalAmount"));
        record.setPaidAmount((BigDecimal) statistics.get("paidAmount"));
        record.setUnpaidAmount((BigDecimal) statistics.get("unpaidAmount"));
        
        return save(record);
    }
}
