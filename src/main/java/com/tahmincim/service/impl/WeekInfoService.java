package com.tahmincim.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tahmincim.model.tahminci.WeekInfo;
import com.tahmincim.repository.WeekInfoRepository;

@Service
public class WeekInfoService {

	@Autowired
	private WeekInfoRepository repository;
	
	public void saveWeek(WeekInfo info) {
		repository.save(info);
	}
	
	public void generateNewWeek(WeekInfo week) {
		WeekInfo lastWeek = new WeekInfo(getCurrentWeek(true));
		lastWeek.setWeekAvailable(false);
		saveWeek(lastWeek);
		week.setWeekAvailable(true);
		week.setPrevWeek(lastWeek);
		saveWeek(week);
	}
	
	public BigDecimal getCurrentWeek(boolean isAvailable) {
		return repository.getCurrentWeek(isAvailable);
	}
	
	public List<BigDecimal> getWeeks() {
		return repository.getWeeks();
	}
	
	public WeekInfo getWeek(BigDecimal weekId) {
		return repository.findById(weekId).get();
	}
}
