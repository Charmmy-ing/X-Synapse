package com.charmmy.x_synapse.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }
    public static <T> Result<T> success() {
        return new Result<>(0, "操作成功", null);
    }
    public static <T> Result<T> error(String message) {
        return new Result<>(1, message, null);
    }
}
