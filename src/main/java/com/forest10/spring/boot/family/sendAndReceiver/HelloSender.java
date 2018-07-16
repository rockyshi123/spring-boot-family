package com.forest10.spring.boot.family.sendAndReceiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Forest10
 * @date 2018/7/16 下午5:19
 */
@Slf4j
@Component
public class HelloSender {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Scheduled(fixedRate = 3000)
	public void send() {
		String content = "hello " + LocalDateTime.now();
		log.info("发送:" + content);
		amqpTemplate.convertAndSend("Forest10", content);
	}

}
