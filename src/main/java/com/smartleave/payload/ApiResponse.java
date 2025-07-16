package com.smartleave.payload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;
}
