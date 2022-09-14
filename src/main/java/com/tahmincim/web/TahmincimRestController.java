package com.tahmincim.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tahmincim.client.IddiaClient;
import com.tahmincim.model.dto.BetDto;
import com.tahmincim.model.dto.MatchInfoDto;
import com.tahmincim.model.dto.UserPointDto;
import com.tahmincim.model.tahminci.MatchInfo;
import com.tahmincim.model.tahminci.Role;
import com.tahmincim.model.tahminci.User;
import com.tahmincim.model.tahminci.WeekInfo;
import com.tahmincim.service.impl.BetService;
import com.tahmincim.service.impl.MatchInfoService;
import com.tahmincim.service.impl.UserService;
import com.tahmincim.service.impl.WeekInfoService;

@RestController
@RequestMapping("/restapi")
@CrossOrigin(origins = "http://localhost:4200")
public class TahmincimRestController {

	@Autowired
	private IddiaClient IddiaClient;

	@Autowired
	private MatchInfoService matchInfoService;

	@Autowired
	private WeekInfoService weekInfoService;

	@Autowired
	private BetService betService;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(method = RequestMethod.POST, value = "/admin/week")
	public ResponseEntity<String> updateWeek(@RequestParam(value = "week") BigDecimal week,
			@RequestParam(name = "closed", defaultValue = "false") boolean closed, Authentication authentication) {
		WeekInfo info = new WeekInfo();
		info.setWeekId(week);
		info.setWeekAvailable(closed);
		weekInfoService.saveWeek(info);
		return ResponseEntity.ok("SUCCESS");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/adduser")
	public ResponseEntity<String> addUser(@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(name = "isAdmin", defaultValue = "false") boolean isAdmin) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.grantAuthority(Role.ROLE_USER);

		if (isAdmin) {
			user.grantAuthority(Role.ROLE_ADMIN);
		}

		userService.addUser(user);

		return ResponseEntity.ok("SUCCESS");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/register")
	public ResponseEntity<String> registerUser(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("refCode") String refCode) {

		if (!"MGUNLER".equalsIgnoreCase(refCode)) {
			return ResponseEntity.badRequest().body("Hatalı Referans Kodu!");
		}
		try {
			userService.loadUserByUsername(username);
		} catch (Exception e) {
			User user = new User();
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(password));
			user.grantAuthority(Role.ROLE_USER);
			userService.addUser(user);

			return ResponseEntity.ok("SUCCESS");
		}

		return ResponseEntity.badRequest().body("Sistemde aynı adlı kullanıcı mevcuttur");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/changepassword")
	public ResponseEntity<String> changePassword(Authentication authentication,
			@RequestParam("password") String password) {

		User user = new User();
		user.setUsername(authentication.getName());
		user.setPassword(passwordEncoder.encode(password));
		user.grantAuthority(Role.ROLE_USER);
		userService.addUser(user);

		return ResponseEntity.ok("SUCCESS");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/users")
	public List<String> getUserList(Authentication authentication) {
		return userService.getUsers();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/weeks")
	public List<BigDecimal> getWeeks(Authentication authentication) {
		return weekInfoService.getWeeks();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/matchinfos")
	public List<MatchInfoDto> getMatchInfos(@RequestBody WeekInfo week, Authentication authentication) {
		return matchInfoService.getMatchInfos(week.getWeekId());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/updatematchinfos")
	public ResponseEntity<String> updateMatchInfos(@RequestBody List<MatchInfoDto> matchList) {
		matchInfoService.addMatches(matchList);
		userService.calculateUserPoints(matchList);
		WeekInfo week = weekInfoService.getWeek(matchList.get(0).getWeekId());
		userService.generateProfiles(week);
		return ResponseEntity.ok("SUCCESS");
	}

	// http://localhost:8080/restapi/loadmatches?week=11
	@RequestMapping(method = RequestMethod.POST, value = "/admin/loadmatches")
	public ResponseEntity<String> loadMatches(@RequestBody WeekInfo week, Authentication authentication) {
		List<MatchInfo> matches = IddiaClient.getFixture(week.getWeekId());
		List<MatchInfoDto> dtos = new ArrayList<MatchInfoDto>();
		weekInfoService.generateNewWeek(week);
		matches.stream().forEach(match -> dtos.add(MatchInfoService.mapDto(match)));
		matchInfoService.addMatches(dtos);
		return ResponseEntity.ok("SUCCESS");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ResponseEntity<String> userLogin(HttpServletRequest request) {
		String userRole = "NONE";
		if (request.isUserInRole("ROLE_ADMIN")) {
			userRole = "ADMIN";
		} else if (request.isUserInRole("ROLE_USER")) {
			userRole = "USER";
		}
		return ResponseEntity.ok(userRole);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/changepassword")
	public ResponseEntity<String> changePassword(@RequestParam("password") String password,
			Authentication authentication) {
		return ResponseEntity.ok("SUCCESS");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/matchlist")
	public List<BetDto> getBetList(@RequestParam(name = "current") boolean current, Authentication authentication) {

		BigDecimal weekId = weekInfoService.getCurrentWeek(current);
		return betService.initializeBets(weekId, current, authentication.getName());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/bets")
	public ResponseEntity<String> setBets(@RequestBody List<BetDto> bets, Authentication authentication) {
		boolean isSuccess = betService.setBets(bets, authentication.getName());
		if (isSuccess) {
			return ResponseEntity.ok("SUCCESS");
		} else {
			return new ResponseEntity<>("Yetersiz puan", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/balance")
	public BigDecimal getAvailableBalance(Authentication authentication) {
		return userService.getAvailableBalance(authentication.getName());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/upcoming")
	public MatchInfoDto upcomingMatch() {
		return matchInfoService.getUpcomingMatch();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/scoreboard/weekly")
	public List<UserPointDto> getWeeklyScoreboard(
			@RequestParam(name = "current", defaultValue = "false") boolean current) {
		BigDecimal weekId = weekInfoService.getCurrentWeek(current);
		return userService.getWeeklyScoreboard(weekId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/scoreboard/total")
	public List<UserPointDto> getTotalScoreboard() {
		return userService.getTotalScoreboard();
	}
}
