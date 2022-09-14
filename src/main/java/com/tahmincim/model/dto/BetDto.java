package com.tahmincim.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BetDto {
	
	private BigDecimal matchId;
	private String matchName;
	private BigDecimal homeOdd;
	private BigDecimal evenOdd;
	private BigDecimal awayOdd;
	private BigDecimal result;
	private BigDecimal minBet = BigDecimal.ONE;
	private BigDecimal amount;
	private BigDecimal decision;
	private BigDecimal weekId;
	private Date startDate;
	private BigDecimal realOdd;
	private BigDecimal totalBets;
	private BigDecimal hitters;
	private BigDecimal point = BigDecimal.ZERO;

}
