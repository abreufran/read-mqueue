package com.example.read_mqueue.models;

import com.example.read_mqueue.dtos.ReadQueueStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Message {
	private ReadQueueStatus status;
	private String message;
	
}
