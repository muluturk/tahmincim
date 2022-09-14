package com.tahmincim.model.iddia;

import lombok.ToString;

@lombok.Data
@ToString
public class Iddia {
	private Data data;

	private String message;

	private String error;

	private String isSuccess;

	private String info;

}