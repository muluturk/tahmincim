package com.tahmincim.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MatchInfoDto{
	
	private BigDecimal matchId;
	private String matchName;
	private BigDecimal homeOdd;
	private BigDecimal evenOdd;
	private BigDecimal awayOdd;
	private BigDecimal result;
	private BigDecimal minBet = BigDecimal.ONE;
	private BigDecimal weekId;
	private Date startDate;
	private BigDecimal homeId;
	private BigDecimal awayId;

}
