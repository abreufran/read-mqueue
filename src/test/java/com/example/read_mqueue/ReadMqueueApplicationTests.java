package com.example.read_mqueue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.read_mqueue.dtos.ConfigQueueDto;
import com.example.read_mqueue.models.Message;
import com.example.read_mqueue.services.ReadMessageService;

@SpringBootTest
class ReadMqueueApplicationTests {
	
	@Autowired
	private ReadMessageService readMessagesService;

	@Test
	void readMessagesTest() {
		/*ConfigQueueDto dto = ConfigQueueDto.builder()
								.host("172.18.150.39")
								.port("1412")
								.channel("QC_CROSS_JDA")
								.queueName("QR_CROSS_JDA_REQ")
								.manager("QM_TEST")
								.build();*/
		
		ConfigQueueDto dto = ConfigQueueDto.builder()
				.host("172.18.150.39")
				.port("1412")
				.channel("QC_RFN_EOM")
				.queueName("QL_RFN_EOM_REQ")
				.manager("QM_TEST")
				.build();
		
		Message message = readMessagesService.readMessages(dto);
		
		System.out.println(message);

	}

}
