package com.tahmincim.model.iddia;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventGroup {
	
	private List<EventResponse> eventResponse;
}
