package com.tahmincim.model.tahminci;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class UserProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal profileId;
	
	@ManyToOne
	@JoinColumn(name = "USERNAME")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "WEEK_ID")
	private WeekInfo weekInfo;
	
	private BigDecimal totalPoint;
	private BigDecimal totalHit;
	private BigDecimal thisWeekPoint;
	private BigDecimal prevWeekPoint;
	private BigDecimal thisWeekHit;
	private BigDecimal prevWeekHit;
	private Integer hitOrder;
	private Integer pointOrder;
	private boolean isLastProfile = true;

}
