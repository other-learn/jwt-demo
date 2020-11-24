package com.lm.example.jwtdemo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SysConstants {

    /**
     * 结果状态
     */
    @Getter
    @AllArgsConstructor
    enum Result {
        SUCCESS(0,"成功"),
        ERROR(-1,"未知错误"),
        ;
        private final Integer code;
        private final String msg;
    }
}
