package com.tahmincim.service;

import java.math.BigDecimal;
import java.util.List;

import com.tahmincim.model.dto.MatchInfoDto;

public interface MatchInfoService {
	
	void addMatches(List<MatchInfoDto> matchDtos);
	List<MatchInfoDto> getMatchInfos(BigDecimal weekId);
	MatchInfoDto getUpcomingMatch();

}
