package com.zheng.hotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zheng.hotel.configuration.exception.BusinessException;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private T data;
    private boolean success;
    private String msg;

    public Result(T data, boolean success, String msg) {
        this.data = data;
        this.success = success;
        this.msg = msg;
    }

    public static Result error(String msg) {
        return new Result<>(null, false, msg);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(data, false, null);
    }

    public static BusinessException badRequestException(String msg) {
        return new BusinessException(Result.badRequest(msg));
    }

    public static <T> BusinessException badRequestException(T t) {
        return new BusinessException(Result.badRequest(t));
    }

    public static <T> ResponseEntity<Result<T>> badRequest(T t) {
        return ResponseEntity.badRequest().body(Result.error(t));
    }

    public static ResponseEntity<Result> badRequest(String msg) {
        return ResponseEntity.badRequest().body(Result.error(msg));
    }


    public static ResponseEntity ok() {
        return Result.ok("操作成功");
    }

    public static <T> ResponseEntity<Result<T>> ok(T data) {
        return ResponseEntity.ok(new Result<>(data, true, null));
    }

    public static ResponseEntity ok(String msg) {
        return ResponseEntity.ok(new Result<>(null, true, msg));
    }


}
