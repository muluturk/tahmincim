package com.tahmincim.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tahmincim.model.tahminci.UserPoint;

public interface UserPointRepository extends CrudRepository<UserPoint, BigDecimal> {

	@Query("select totalPoint from UserProfile u where isLastProfile = true and user.username = :username")
	public BigDecimal getAvailableBalance(@Param("username") String username);

	@Query("from UserPoint u where weekInfo.weekId = :weekId order by point desc")
	public List<UserPoint> getWeeklyScoreboard(@Param("weekId") BigDecimal weekId);
	
	@Query("from UserPoint where user.username = :username and weekInfo.weekId = :weekId")
	public UserPoint getUserPointByWeekId(@Param("username") String username, @Param("weekId") BigDecimal weekId);

}
