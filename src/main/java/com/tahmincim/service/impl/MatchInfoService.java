package com.tahmincim.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tahmincim.model.dto.MatchInfoDto;
import com.tahmincim.model.tahminci.MatchInfo;
import com.tahmincim.model.tahminci.WeekInfo;
import com.tahmincim.repository.MatchInfoRepository;

@Service
public class MatchInfoService {

	@Autowired
	private MatchInfoRepository repository;

	public void addMatches(List<MatchInfoDto> matchDtos) {
		List<MatchInfo> matches = new ArrayList<MatchInfo>();
		matchDtos.stream().forEach(match -> {
			matches.add(mapPojo(match));
		});
		repository.saveAll(matches);
	}

	public List<MatchInfoDto> getMatchInfos(BigDecimal weekId) {
		List<MatchInfo> matchesOfWeek = repository.getAllMatchesOfWeek(weekId);
		List<MatchInfoDto> dtos = new ArrayList<MatchInfoDto>();

		for (MatchInfo match : matchesOfWeek) {
			dtos.add(mapDto(match));
		}

		return dtos;
	}
	
	public MatchInfoDto getUpcomingMatch() {
		List<MatchInfo> upcomingMatches = repository.getUpcomingMatches();
		
		if (upcomingMatches != null && upcomingMatches.size() > 0) {
			return mapDto(upcomingMatches.get(0));
		}
		
		return null;
	}

	public static MatchInfoDto mapDto(MatchInfo match) {
		MatchInfoDto dto = new MatchInfoDto();
		dto.setAwayOdd(match.getAwayOdd());
		dto.setHomeOdd(match.getHomeOdd());
		dto.setEvenOdd(match.getEvenOdd());
		dto.setMatchId(match.getMatchId());
		dto.setMatchName(match.getMatchName());
		dto.setMinBet(match.getMinBet());
		dto.setResult(match.getResult());
		dto.setWeekId(match.getWeekInfo().getWeekId());
		dto.setStartDate(match.getStartDate());
		dto.setHomeId(match.getHomeId());
		dto.setAwayId(match.getAwayId());
		
		return dto;
	}

	public static MatchInfo mapPojo(MatchInfoDto dto) {
		MatchInfo info = new MatchInfo();
		info.setAwayOdd(dto.getAwayOdd());
		info.setEvenOdd(dto.getEvenOdd());
		info.setHomeOdd(dto.getHomeOdd());
		info.setMatchId(dto.getMatchId());
		info.setMatchName(dto.getMatchName());
		info.setResult(dto.getResult());
		info.setWeekInfo(new WeekInfo(dto.getWeekId()));
		info.setStartDate(dto.getStartDate());
		info.setHomeId(dto.getHomeId());
		info.setAwayId(dto.getAwayId());
		return info;
	}

}
