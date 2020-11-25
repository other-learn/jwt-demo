package com.lm.example.jwtdemo.handler.exception;

import com.lm.example.jwtdemo.model.vo.ResultVO;
import com.lm.example.jwtdemo.util.ResultTool;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

/**
 * 统一异常处理类
 * @author meng-liang
 */
@RestControllerAdvice
public class PermissionHandler {
    @ExceptionHandler(value = { SignatureException.class })
    @ResponseBody
    public ResultVO authorizationException(SignatureException e){
        return ResultTool.error(e.getMessage());
    }
}
