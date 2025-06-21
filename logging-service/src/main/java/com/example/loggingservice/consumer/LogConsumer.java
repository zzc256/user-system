package com.example.loggingservice.consumer;

import com.example.loggingservice.common.OperationLogEvent;
import com.example.loggingservice.entity.OperationLog;
import com.example.loggingservice.mapper.OperationLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LogConsumer {
    @Autowired
    private OperationLogMapper logMapper;
    @RabbitListener(queues = "log.queue")
    public void handle(OperationLogEvent event) {
        OperationLog log = new OperationLog();
        log.setUserId(event.getUserId());
        log.setAction(event.getAction());
        log.setIp(event.getIp());
        log.setDetail(event.getDetail());
        logMapper.insert(log);
    }
}
