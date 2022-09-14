package com.tahmincim.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tahmincim.model.tahminci.MatchInfo;
import com.tahmincim.model.tahminci.User;

public interface UserRepository extends CrudRepository<User, String> {
	
	@Query("from MatchInfo where weekInfo.weekId = :weekId")
	public List<MatchInfo> getMatchList(@Param("weekId") BigDecimal weekId);

}
