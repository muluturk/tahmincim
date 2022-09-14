package com.tahmincim.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tahmincim.model.tahminci.Bet;

public interface BetRepository extends CrudRepository<Bet, BigDecimal> {

	@Query("from Bet where id.user.username = :username and weekInfo.weekId = :weekId")
	public List<Bet> getBets(@Param("username") String username, @Param("weekId") BigDecimal weekId);

	@Query("from Bet where id.matchInfo.matchId = :matchId")
	public List<Bet> getBetsByMatchId(@Param("matchId") BigDecimal matchId);

	@Query("select count(*) from Bet where decision = :decision and id.matchInfo.matchId = :matchId")
	public BigDecimal getCountHitters(@Param("decision") BigDecimal decision, @Param("matchId") BigDecimal matchId);

	@Query("select count(*) from Bet where id.matchInfo.matchId = :matchId and amount > 0")
	public BigDecimal getCountTotalBets(@Param("matchId") BigDecimal matchId);

}
