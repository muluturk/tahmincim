package com.tahmincim.service;

import java.math.BigDecimal;
import java.util.List;

import com.tahmincim.model.dto.BetDto;

public interface BetService {

	List<BetDto> initializeBets(BigDecimal weekId, Boolean current, String username);

	boolean setBets(List<BetDto> dtos, String username);
}
