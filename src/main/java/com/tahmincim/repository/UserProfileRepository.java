package com.tahmincim.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tahmincim.model.tahminci.UserProfile;

public interface UserProfileRepository extends CrudRepository<UserProfile, BigDecimal> {

	@Query("from UserProfile u where isLastProfile = true and user.username = :username")
	public UserProfile getPreviousProfile(@Param("username") String username);

	@Query("from UserProfile u where isLastProfile = true order by totalPoint desc")
	public List<UserProfile> getProfiles();
}
