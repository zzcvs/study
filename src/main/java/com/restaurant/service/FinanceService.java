package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.FinanceRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FinanceService extends IService<FinanceRecord> {
    
    List<FinanceRecord> getFinanceRecordsByDate(LocalDate date);
    
    Map<String, Object> getDailyStatistics(LocalDate date);
    
    Map<String, Object> getMonthlyStatistics(int year, int month);
    
    boolean generateDailyFinanceRecord(LocalDate date);
}
