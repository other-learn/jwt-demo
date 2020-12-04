package com.lm.example.jwtdemo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author meng-liang
 */
public interface AuthConstants {

    /**
     * 结果状态
     */
    @Getter
    @AllArgsConstructor
    enum AuthorizationEnum {
        SELECT(0,"必须拥有查询权限"),
        INSERT(1,"必须拥有查询权限 && 新增权限"),
        UPDATE(2,"必须拥有查询权限 && 修改权限"),
        DELETE(3,"拥有查询权限 && 删除权限"),
        NULL(-1,"基础功能方法,访客什么权限都不需要有都可以请求当前注解的方法"),
        ;
        private final Integer code;
        private final String msg;
    }
}
