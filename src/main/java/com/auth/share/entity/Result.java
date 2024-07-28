package com.auth.share.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private T data;
    private boolean isSuccess;

    public static <T> Result<T> success(T data) {
        return new Result<T>(data, true);
    }

    public static <T> Result<T> failed(T data) {
        return new Result<T>(data, false);
    }
}
