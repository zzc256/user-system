package com.example.loggingservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogEvent implements Serializable {

    private Long userId;
    private String action;
    private String ip;
    private String detail;

}