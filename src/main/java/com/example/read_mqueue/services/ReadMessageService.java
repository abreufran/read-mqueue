package com.example.read_mqueue.services;

import org.springframework.stereotype.Service;

import com.example.read_mqueue.dtos.ConfigQueueDto;
import com.example.read_mqueue.dtos.ReadQueueStatus;
import com.example.read_mqueue.models.Message;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReadMessageService {
	
	public Message readMessages(ConfigQueueDto configQueueDto) {
		Message message = null;
		MQQueueManager queueManager = null;
		MQQueue mqQueue = null;
		MQMessage mqMessage = null;

		try {
			int openOptions = 33;

			MQEnvironment.hostname = configQueueDto.getHost();
			MQEnvironment.channel = configQueueDto.getChannel();
			MQEnvironment.port = Integer.parseInt(configQueueDto.getPort());

			queueManager = new MQQueueManager(configQueueDto.getManager());
			mqQueue = queueManager.accessQueue(configQueueDto.getQueueName(), openOptions, null, null, null);

			MQGetMessageOptions mqGetMessageOptions = new MQGetMessageOptions();

			//mqGetMessageOptions.options = 4;
			mqGetMessageOptions.waitInterval = 5000;
			
			mqGetMessageOptions.options = CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING;
		
			
			if(mqQueue.getCurrentDepth() == 0) {
				message = Message.builder()
						.status(ReadQueueStatus.OK)
						.build();
			}
			else {
				mqMessage = new MQMessage();
				mqQueue.get(mqMessage, mqGetMessageOptions);
				
				if(validateMessageFormat(mqMessage.format)) {
					int strLen = mqMessage.getMessageLength();
					
					String msgText = mqMessage.readStringOfByteLength(strLen);
	
					log.debug("Queue Message: " + msgText);
					message = Message.builder()
							.message(msgText)
							.status(ReadQueueStatus.OK)
							.build();
				}
				else {
					message = Message.builder()
							.status(ReadQueueStatus.ERROR_READING_QUEUE)
							.build();
				}
			}
				
			mqQueue.close();
			queueManager.disconnect();

		} catch (Exception err) {
			log.error("An error occurred while reading the queue.", err);
			
			message = Message.builder()
					.status(ReadQueueStatus.ERROR_READING_QUEUE)
					.build();
		} finally {
			if (mqQueue != null) {
				try {
					mqQueue.close();
				} catch (MQException e) {
					log.error("The queue could not be closed.", e);
				}
			}
			if (queueManager != null) {
				try {
					queueManager.disconnect();
				} catch (MQException e) {
					log.error("The queueManager could not be closed.", e);
				}
			}
		}
		
		log.debug("IbmMessage read from mQueue: " + message);

		return message;

	}
	
	protected boolean validateMessageFormat(String format) {
		return "MQSTR   ".equals(format);
	}
}
