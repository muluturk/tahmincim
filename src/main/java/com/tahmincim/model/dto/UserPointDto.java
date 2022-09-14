package com.tahmincim.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserPointDto {
	private String username;
	private BigDecimal weekId;
	private BigDecimal point;
	private BigDecimal hit;
	private BigDecimal totalHit;
	private BigDecimal totalPoint;
	private Integer hitOrder;
	private Integer pointOrder;
}
