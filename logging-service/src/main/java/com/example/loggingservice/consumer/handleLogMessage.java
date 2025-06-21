package com.example.loggingservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "log.queue")
public class handleLogMessage {
}
