package com.tahmincim.model.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
	
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

}
