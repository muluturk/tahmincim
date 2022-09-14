package com.tahmincim.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tahmincim.model.dto.BetDto;
import com.tahmincim.model.tahminci.Bet;
import com.tahmincim.model.tahminci.BetId;
import com.tahmincim.model.tahminci.MatchInfo;
import com.tahmincim.model.tahminci.User;
import com.tahmincim.model.tahminci.WeekInfo;
import com.tahmincim.repository.BetRepository;
import com.tahmincim.repository.MatchInfoRepository;

@Service
public class BetService {

	@Autowired
	private BetRepository repository;

	@Autowired
	private MatchInfoRepository matchInfoRepository;

	@Autowired
	private UserService userService;

	public List<BetDto> initializeBets(BigDecimal weekId, Boolean current, String username) {
		List<BetDto> dtos = new ArrayList<BetDto>();

		List<Bet> bets = repository.getBets(username, weekId);
		List<MatchInfo> matchesOfWeek = matchInfoRepository.getAllMatchesOfWeek(weekId);

		if (current) {

			if (bets.isEmpty()) {
				bets = new ArrayList<Bet>();

				for (MatchInfo match : matchesOfWeek) {
					Bet bet = new Bet();
					BetId id = new BetId(match, new User(username));
					bet.setId(id);
					bet.setWeekInfo(new WeekInfo(weekId));
					bet.setBetDate(new Date());

					repository.save(bet);
					bets.add(bet);
				}
			}

			bets.stream().forEach(bet -> dtos.add(mapDto(bet)));
		} else {
			bets.stream().forEach(bet -> {
				BetDto dto = mapDto(bet);

				MatchInfo match = matchInfoRepository.findById(bet.getId().getMatchInfo().getMatchId()).get();

				dto.setRealOdd(match.getRealOdd());
				dto.setHitters(match.getHitters());
				dto.setTotalBets(match.getTotalBets());

				if (bet.getDecision() != null) {
					if (match.getResult().compareTo(bet.getDecision()) == 0) {
						BigDecimal point = bet.getAmount().multiply(dto.getRealOdd()).setScale(2, RoundingMode.CEILING);

						dto.setPoint(point.subtract(bet.getAmount()).add(BigDecimal.ONE));
					} else {
						dto.setPoint(BigDecimal.ONE.subtract(bet.getAmount()));
					}
				}
				dtos.add(dto);
			});

		}

		return dtos;

	}

	private BetDto mapDto(Bet bet) {
		BetDto dto = new BetDto();
		dto.setAwayOdd(bet.getId().getMatchInfo().getAwayOdd());
		dto.setHomeOdd(bet.getId().getMatchInfo().getHomeOdd());
		dto.setEvenOdd(bet.getId().getMatchInfo().getEvenOdd());
		dto.setMatchId(bet.getId().getMatchInfo().getMatchId());
		dto.setMatchName(bet.getId().getMatchInfo().getMatchName());
		dto.setMinBet(bet.getMinBet());
		dto.setResult(bet.getId().getMatchInfo().getResult());
		dto.setAmount(bet.getAmount());
		dto.setDecision(bet.getDecision());
		dto.setWeekId(bet.getWeekInfo().getWeekId());
		dto.setStartDate(bet.getId().getMatchInfo().getStartDate());
		return dto;
	}

	public boolean setBets(List<BetDto> dtos, String username) {

		BigDecimal balance = BigDecimal.ZERO;
		BigDecimal availableBalance = BigDecimal.ZERO;
		List<Bet> dbBets = repository.getBets(username, dtos.get(0).getWeekId());

		for (BetDto dto : dtos) {
			for (Bet bet : dbBets) {
				if (dto.getMatchId().compareTo(bet.getId().getMatchInfo().getMatchId()) == 0) {
					if (matchInfoRepository.validateMatch(dto.getMatchId()) != null) {
						bet.setAmount(dto.getAmount());
						bet.setBetDate(new Date());
						bet.setDecision(dto.getDecision());
					}
				}
			}

			if (dbBets == null || dbBets.isEmpty()) {
				balance = balance.add(dto.getAmount() == null ? BigDecimal.ZERO : dto.getAmount());
			}
		}

		for (Bet bet : dbBets) {
			if (bet.getAmount() != null && bet.getAmount().signum() > 0) {
				availableBalance = availableBalance.add(BigDecimal.ONE);
			}
			balance = balance.add(bet.getAmount() == null ? BigDecimal.ZERO : bet.getAmount());
		}

		availableBalance = availableBalance.add(userService.getAvailableBalance(username));

		if (availableBalance.compareTo(balance) < 0) {
			return false;
		}

		repository.saveAll(dbBets);
		return true;
	}

	private Bet mapPojo(String username, BetDto dto) {
		Bet bet = new Bet();
		User user = new User(username);
		MatchInfo matchInfo = new MatchInfo();
		matchInfo.setMatchId(dto.getMatchId());
		matchInfo.setMatchName(dto.getMatchName());
		matchInfo.setStartDate(dto.getStartDate());
		BetId id = new BetId(matchInfo, user);
		bet.setId(id);
		bet.setBetDate(new Date());
		bet.setAmount(dto.getAmount());
		bet.setDecision(dto.getDecision());
		WeekInfo week = new WeekInfo(dto.getWeekId());
		bet.setWeekInfo(week);
		return bet;
	}

}
