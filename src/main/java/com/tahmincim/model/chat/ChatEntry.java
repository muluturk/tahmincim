package com.tahmincim.model.chat;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tahmincim.model.tahminci.User;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class ChatEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal entryId;
	
	@ManyToOne
	@JoinColumn(name = "USERNAME")
	private User user;
	
	private String text;

}
