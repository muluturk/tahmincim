package com.tahmincim.model.tahminci;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class MatchInfo {

	@Id
	private BigDecimal matchId;
	
	@OneToOne
	@JoinColumn(name = "WEEK_ID")
	private WeekInfo weekInfo;
	
	private String matchName;
	private BigDecimal homeOdd;
	private BigDecimal evenOdd;
	private BigDecimal awayOdd;
	private BigDecimal result;
	private BigDecimal minBet = BigDecimal.ONE;
	private Date startDate;
	private BigDecimal homeId;
	private BigDecimal awayId;
	private BigDecimal realOdd;
	private BigDecimal totalBets;
	private BigDecimal hitters;
	
}
