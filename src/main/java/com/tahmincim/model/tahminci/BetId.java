package com.tahmincim.model.tahminci;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class BetId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "MATCH_ID")
	private MatchInfo matchInfo;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	public BetId() {
		super();
	}

	public BetId(MatchInfo matchInfo, User user) {
		super();
		this.matchInfo = matchInfo;
		this.user = user;
	}

	public MatchInfo getMatchInfo() {
		return matchInfo;
	}

	public void setMatchInfo(MatchInfo matchInfo) {
		this.matchInfo = matchInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
