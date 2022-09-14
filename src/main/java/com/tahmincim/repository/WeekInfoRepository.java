package com.tahmincim.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tahmincim.model.tahminci.WeekInfo;

public interface WeekInfoRepository extends CrudRepository<WeekInfo, BigDecimal> {
	
	@Query("select max(weekId) from WeekInfo where isWeekAvailable = :isAvailable")
	public BigDecimal getCurrentWeek(@Param("isAvailable") boolean isAvailable);
	
	@Query("select weekId from WeekInfo")
	public List<BigDecimal> getWeeks();

}
