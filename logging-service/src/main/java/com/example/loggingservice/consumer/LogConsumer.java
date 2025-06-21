package com.example.loggingservice.consumer;

import com.example.loggingservice.common.OperationLogEvent;
import com.example.loggingservice.entity.OperationLog;
import com.example.loggingservice.mapper.OperationLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LogConsumer {
    private final OperationLogMapper operationLogMapper;

    @RabbitListener(queues = "log.queue")
    public void handle(OperationLogEvent event) {
        OperationLog log = new OperationLog();
        log.setId(event.getUserId());
        log.setUserId(event.getUserId());
        log.setAction(event.getAction());
        log.setIp(event.getIp());
        log.setDetail(event.getDetail());
        log.setGmtCreate(LocalDateTime.now());
        operationLogMapper.insert(log);
    }
}
