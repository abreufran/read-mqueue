package com.example.read_mqueue.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString
public class ConfigQueueDto {

	
	private String host;

	private String port;
	
	private String channel;
	
	private String queueName;

	private String manager;
	
}
