package com.tahmincim.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tahmincim.model.tahminci.MatchInfo;

public interface MatchInfoRepository extends CrudRepository<MatchInfo, BigDecimal> {

	@Query("from MatchInfo where weekInfo.weekId = :weekId")
	public List<MatchInfo> getAllMatchesOfWeek(@Param("weekId") BigDecimal weekId);
	
	@Query("select matchId from MatchInfo where matchId = :matchId and startDate > CURRENT_DATE")
	public BigDecimal validateMatch(@Param("matchId") BigDecimal matchId);
	
	@Query("from MatchInfo where startDate > CURRENT_DATE order by startDate")
	public List<MatchInfo> getUpcomingMatches();
	
}
