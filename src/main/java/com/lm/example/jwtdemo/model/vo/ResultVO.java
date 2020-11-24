package com.lm.example.jwtdemo.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 统一返回类型
 * @author meng-liang
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;
}
