package com.tahmincim.service;

import java.math.BigDecimal;
import java.util.List;

import com.tahmincim.model.tahminci.WeekInfo;

public interface WeekInfoService {
	void saveWeek(WeekInfo info);
	void generateNewWeek(WeekInfo week);
	BigDecimal getCurrentWeek(boolean isAvailable);
	List<BigDecimal> getWeeks();
	WeekInfo getWeek(BigDecimal weekId);

}
