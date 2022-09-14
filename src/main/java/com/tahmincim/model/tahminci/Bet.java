package com.tahmincim.model.tahminci;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Bet {

	@EmbeddedId
	private BetId id;

	private BigDecimal amount;
	private BigDecimal decision;
	private Date betDate;
	private BigDecimal minBet = BigDecimal.ONE;

	@OneToOne
	@JoinColumn(name = "WEEK_ID")
	private WeekInfo weekInfo;

	public BetId getId() {
		return id;
	}

	public void setId(BetId id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDecision() {
		return decision;
	}

	public void setDecision(BigDecimal decision) {
		this.decision = decision;
	}

	public Date getBetDate() {
		return betDate;
	}

	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}

	public WeekInfo getWeekInfo() {
		return weekInfo;
	}

	public void setWeekInfo(WeekInfo weekInfo) {
		this.weekInfo = weekInfo;
	}

	public BigDecimal getMinBet() {
		return minBet;
	}

	public void setMinBet(BigDecimal minBet) {
		this.minBet = minBet;
	}

}
