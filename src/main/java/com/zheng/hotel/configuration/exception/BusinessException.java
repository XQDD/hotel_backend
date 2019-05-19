package com.zheng.hotel.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

//业务错误
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private ResponseEntity responseEntity;
}
