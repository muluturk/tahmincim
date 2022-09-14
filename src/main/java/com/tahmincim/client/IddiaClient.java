package com.tahmincim.client;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tahmincim.model.iddia.EventGroup;
import com.tahmincim.model.iddia.EventResponse;
import com.tahmincim.model.iddia.Iddia;
import com.tahmincim.model.iddia.Spg;
import com.tahmincim.model.tahminci.MatchInfo;
import com.tahmincim.model.tahminci.WeekInfo;

@Service
public class IddiaClient {

	@Autowired
	RestTemplate restTemplate;

	public List<MatchInfo> getFixture(BigDecimal weekId) {
		List<MatchInfo> matches = new ArrayList<MatchInfo>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>("[\"1_1\",\"2_87\",\"2_7_2.5\",\"2_101_2.5\",\"2_89\"]",
				headers);
		//
		ResponseEntity<Iddia> responseEntity = restTemplate.exchange(
				"https://sportprogram.iddaa.com/SportProgram?ProgramType=1&SportId=1&MukList=1_1&GroupByLeague=true",
				HttpMethod.GET, request, Iddia.class);

		Iddia iddia = responseEntity.getBody();

		if (iddia != null) {
			for (Spg spg : iddia.getData().getSpg()) {
				for (EventGroup group : spg.getEventGroup()) {
					for (EventResponse event : group.getEventResponse()) {
						if ("Türkiye Süper Lig".equalsIgnoreCase(event.getCn())) {
							MatchInfo info = new MatchInfo();
							info.setMatchName(event.getEn());
							info.setHomeOdd(new BigDecimal(event.getM()[0].getO()[0].getOdd()));
							info.setEvenOdd(new BigDecimal(event.getM()[0].getO()[1].getOdd()));
							info.setAwayOdd(new BigDecimal(event.getM()[0].getO()[2].getOdd()));
							info.setMatchId(new BigDecimal(event.getEid()));
							info.setWeekInfo(new WeekInfo(weekId));
							info.setHomeId(event.getEprt().get(0).getEpid());
							info.setAwayId(event.getEprt().get(1).getEpid());
							try {
								Calendar cal = Calendar.getInstance();
								int year = cal.get(Calendar.YEAR);
								String dateStr = event.getEde();
								// 2019-12-08T19:00:00
								String isoDatePattern = "dd MMMM EEE yyyy HH:mm";
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern, new Locale("tr"));

								info.setStartDate(simpleDateFormat.parse(dateStr + " " + year + " " + event.getEdh()));
								cal.setTime(info.getStartDate());
								cal.add(Calendar.HOUR_OF_DAY, -3);
								info.setStartDate(cal.getTime());
							} catch (Exception e) {
								e.printStackTrace();
							}

							matches.add(info);
							System.out.println(event.getEn() + " " + event.getM()[0].getO()[0].getOdd() + " "
									+ event.getM()[0].getO()[1].getOdd() + " " + event.getM()[0].getO()[2].getOdd());
						}
					}
				}
			}
		}

		return matches;

	}

}
