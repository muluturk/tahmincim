package com.tahmincim.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tahmincim.model.dto.MatchInfoDto;
import com.tahmincim.model.dto.UserPointDto;
import com.tahmincim.model.tahminci.Bet;
import com.tahmincim.model.tahminci.MatchInfo;
import com.tahmincim.model.tahminci.User;
import com.tahmincim.model.tahminci.UserPoint;
import com.tahmincim.model.tahminci.UserProfile;
import com.tahmincim.model.tahminci.WeekInfo;
import com.tahmincim.repository.BetRepository;
import com.tahmincim.repository.MatchInfoRepository;
import com.tahmincim.repository.UserPointRepository;
import com.tahmincim.repository.UserProfileRepository;
import com.tahmincim.repository.UserRepository;
import com.tahmincim.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserPointRepository pointRepository;

	@Autowired
	private BetRepository betRepository;
	
	@Autowired
	private MatchInfoRepository matchInfoRepository;

	@Autowired
	private UserProfileRepository profileRepository;
	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findById(username);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UsernameNotFoundException(username + " adlı kullanıcı bulunamadı!");
		}
	}
	
	@Override
	public List<String> getUsers() {
		List<String> users = new ArrayList<String>();
		userRepository.findAll().forEach(user -> users.add(user.getUsername()));
		return users;
	}

	@Override
	public void addUser(User user) {
		userRepository.save(user);
	}

	@Override
	public BigDecimal getAvailableBalance(String username) {
		BigDecimal availableBalance = pointRepository.getAvailableBalance(username);
		if (availableBalance != null) {
			return availableBalance;
		}

		return BigDecimal.ZERO;
	}

	@Override
	public List<UserPointDto> getWeeklyScoreboard(BigDecimal weekId) {
		List<UserPointDto> dtos = new ArrayList<UserPointDto>();
		List<UserPoint> scoreboard = pointRepository.getWeeklyScoreboard(weekId);
		scoreboard.stream().forEach(score -> dtos.add(mapDto(score)));
		return dtos;
	}

	@Override
	public List<UserPointDto> getTotalScoreboard() {
		List<UserProfile> profiles = profileRepository.getProfiles();
		List<UserPointDto> dtos = new ArrayList<UserPointDto>();
		profiles.stream().forEach(profile -> dtos.add(mapDto(profile)));
		
		return dtos;
	}

	@Override
	public void calculateUserPoints(List<MatchInfoDto> matchDtos) {
		List<MatchInfo> matches = new ArrayList<MatchInfo>();
		matchDtos.stream().forEach(match -> {
			matches.add(MatchInfoServiceImpl.mapPojo(match));
		});
		for (MatchInfo match : matches) {
			BigDecimal countHitters = betRepository.getCountHitters(match.getResult(), match.getMatchId());
			BigDecimal countTotalBets = betRepository.getCountTotalBets(match.getMatchId());

			List<Bet> betList = null;
			if (match.getResult() != null) {
				betList = betRepository.getBetsByMatchId(match.getMatchId());
			}
			BigDecimal ratio = match.getAwayOdd();
			if (BigDecimal.ZERO.equals(match.getResult())) {
				ratio = match.getEvenOdd();
			} else if (BigDecimal.ONE.equals(match.getResult())) {
				ratio = match.getHomeOdd();
			}
			
			logger.debug(match.getMatchName() + " ratio:" + ratio);
			
			boolean isFirstTime = true;

			for (Bet bet : betList) {

				if (bet.getDecision() != null) {
					if (match.getResult().compareTo(bet.getDecision()) == 0) {
						
						if (isFirstTime) {
							BigDecimal realOdd = BigDecimal.ONE
									.multiply((ratio.multiply(new BigDecimal(0.55)))
											.add(countTotalBets.divide(countHitters,2, RoundingMode.HALF_UP).multiply(new BigDecimal(0.45))))
									.setScale(2, RoundingMode.CEILING);
							
							match.setRealOdd(realOdd);
							match.setTotalBets(countTotalBets);
							match.setHitters(countHitters);
							matchInfoRepository.save(match);
						}
						
						BigDecimal point = bet.getAmount()
								.multiply((ratio.multiply(new BigDecimal(0.55)))
										.add(countTotalBets.divide(countHitters,2, RoundingMode.HALF_UP).multiply(new BigDecimal(0.45))))
								.setScale(2, RoundingMode.CEILING);

						UserPoint userPoint = pointRepository.getUserPointByWeekId(bet.getId().getUser().getUsername(),
								bet.getWeekInfo().getWeekId());

						if (userPoint == null) {
														
							userPoint = new UserPoint();
							userPoint.setPoint(point.subtract(bet.getAmount()).add(BigDecimal.ONE));
							userPoint.setGrossPoint(point);
							userPoint.setTotalHit(BigDecimal.ONE);
							userPoint.setUser(bet.getId().getUser());
							userPoint.setWeekInfo(bet.getWeekInfo());
						} else {
							userPoint.setGrossPoint(userPoint.getGrossPoint().add(point));
							userPoint.setPoint(userPoint.getPoint().add(point).subtract(bet.getAmount()).add(BigDecimal.ONE));
							userPoint.setTotalHit(userPoint.getTotalHit().add(BigDecimal.ONE));
						}
						pointRepository.save(userPoint);

					} else if (bet.getDecision() != null) {
						UserPoint userPoint = pointRepository.getUserPointByWeekId(bet.getId().getUser().getUsername(),
								bet.getWeekInfo().getWeekId());

						if (userPoint == null) {
														
							userPoint = new UserPoint();
							userPoint.setPoint(BigDecimal.ONE.subtract(bet.getAmount()));
							userPoint.setGrossPoint(BigDecimal.ONE.subtract(bet.getAmount()));
							userPoint.setTotalHit(BigDecimal.ZERO);
							userPoint.setUser(bet.getId().getUser());
							userPoint.setWeekInfo(bet.getWeekInfo());
						} else {
							userPoint.setGrossPoint(userPoint.getGrossPoint().subtract(bet.getAmount()));
							userPoint.setPoint(userPoint.getPoint().add(BigDecimal.ONE).subtract(bet.getAmount()));
						}
						pointRepository.save(userPoint);
					}
				}

			}

		}
	}

	@Override
	public void generateProfiles(WeekInfo week) {
		List<UserPoint> points = pointRepository.getWeeklyScoreboard(week.getWeekId());
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		Integer order = 0;
		for (UserPoint point : points) {
			UserProfile profile = new UserProfile();
			profile.setUser(point.getUser());
			profile.setWeekInfo(point.getWeekInfo());
			profile.setPointOrder(order++);
			profile.setThisWeekPoint(point.getPoint());
			profile.setThisWeekHit(point.getTotalHit());
			profiles.add(profile);
		}
		order = 0;
		profiles.sort(Comparator.comparing(UserProfile::getThisWeekHit));
		for (UserProfile profile : profiles) {
			profile.setHitOrder(order++);

			UserProfile previousProfile = profileRepository.getPreviousProfile(profile.getUser().getUsername());
			
			if (previousProfile != null) {
				profile.setPrevWeekHit(previousProfile.getThisWeekHit());
				profile.setPrevWeekPoint(previousProfile.getThisWeekPoint());
				profile.setTotalPoint(previousProfile.getTotalPoint().add(profile.getThisWeekPoint()));
				profile.setTotalHit(previousProfile.getTotalHit().add(profile.getThisWeekHit()));
				
				previousProfile.setLastProfile(false);
				profileRepository.save(previousProfile);
			} else {
				profile.setTotalPoint(profile.getThisWeekPoint());
				profile.setTotalHit(profile.getThisWeekHit());
			}
		}

		profileRepository.saveAll(profiles);
	}

	private static UserPointDto mapDto(UserPoint userPoint) {
		UserPointDto dto = new UserPointDto();
		dto.setPoint(userPoint.getPoint());
		dto.setTotalHit(userPoint.getTotalHit());
		dto.setUsername(userPoint.getUser().getUsername());
		dto.setWeekId(userPoint.getWeekInfo().getWeekId());
		return dto;
	}
	
	private static UserPointDto mapDto(UserProfile profile) {
		UserPointDto dto = new UserPointDto();
		dto.setTotalPoint(profile.getTotalPoint());
		dto.setTotalHit(profile.getTotalHit());
		dto.setPoint(profile.getThisWeekPoint());
		dto.setHit(profile.getThisWeekHit());
		dto.setUsername(profile.getUser().getUsername());
		dto.setWeekId(profile.getWeekInfo().getWeekId());
		return dto;
	}

}
