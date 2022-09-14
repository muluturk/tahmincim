package com.tahmincim.model.tahminci;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class WeekInfo {

	@Id
	private BigDecimal weekId;
	private boolean isWeekAvailable;
	
	@OneToOne
	private WeekInfo prevWeek;
	
	public WeekInfo(BigDecimal weekId) {
		super();
		this.weekId = weekId;
	}
}
