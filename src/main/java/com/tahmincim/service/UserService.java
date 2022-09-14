package com.tahmincim.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tahmincim.model.dto.MatchInfoDto;
import com.tahmincim.model.dto.UserPointDto;
import com.tahmincim.model.tahminci.User;
import com.tahmincim.model.tahminci.WeekInfo;

public interface UserService extends UserDetailsService{

	List<String> getUsers();
	void addUser(User user);
	BigDecimal getAvailableBalance(String username);
	List<UserPointDto> getWeeklyScoreboard(BigDecimal weekId);
	List<UserPointDto> getTotalScoreboard();
	void calculateUserPoints(List<MatchInfoDto> matchDtos);
	void generateProfiles(WeekInfo week);
}
