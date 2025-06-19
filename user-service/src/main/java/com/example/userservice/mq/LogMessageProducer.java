package com.example.userservice.mq;


import com.example.common.OperationLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "log.exchange";
    private static final String ROUTING_KEY = "log.routing.key";


    public void sendLog(OperationLogEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
    }

}
