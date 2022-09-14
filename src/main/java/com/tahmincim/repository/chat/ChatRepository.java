package com.tahmincim.repository.chat;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.tahmincim.model.chat.ChatEntry;

public interface ChatRepository extends CrudRepository<ChatEntry, BigDecimal> {

}
