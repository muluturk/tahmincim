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
import lombok.ToString;

@Entity
@Data
@ToString
public class UserPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal pointId;

	@ManyToOne
	@JoinColumn(name = "USERNAME")
	private User user;

	@OneToOne
	@JoinColumn(name = "WEEK_ID")
	private WeekInfo weekInfo;

	private BigDecimal point = BigDecimal.ZERO;
	private BigDecimal grossPoint = BigDecimal.ZERO;
	private BigDecimal totalHit = BigDecimal.ZERO;

}
