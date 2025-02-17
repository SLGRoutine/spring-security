package com.rjgf.entity.web.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ResponseResult implements Serializable {
    private Integer code; //  200：成功  其他为失败
    private String msg;
    private Object data;
    private Long count;

    public static ResponseResult success(String msg) {
        return ResponseResult.builder().code(200).msg(msg).count(0L).build();
    }

    public static ResponseResult success() {
        return ResponseResult.builder().code(200).msg("成功").count(0L).build();
    }

    public static ResponseResult failure(String msg) {
        return ResponseResult.builder().code(500).msg(msg).count(0L).build();
    }

    public static ResponseResult failure() {
        return ResponseResult.builder().code(500).msg("失败").count(0L).build();
    }

    public <T> ResponseResult setData(T data) {
        this.data = data;
        return this;
    }

    public ResponseResult setCount(Long count) {
        this.count = count;
        return this;
    }
    }
