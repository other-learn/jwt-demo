package com.lm.example.jwtdemo.util;

import com.lm.example.jwtdemo.constants.SysConstants;
import com.lm.example.jwtdemo.model.vo.ResultVO;
import org.springframework.util.StringUtils;

/**
 * @author meng-liang
 */
public class ResultTool<T> {
    public static <T> ResultVO<T> success(T data) {
        ResultVO result = new ResultVO();
        result.setCode(SysConstants.Result.SUCCESS.getCode());
        result.setMsg(SysConstants.Result.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static ResultVO success() {
        ResultVO result = new ResultVO();
        result.setCode(SysConstants.Result.SUCCESS.getCode());
        result.setMsg(SysConstants.Result.SUCCESS.getMsg());
        return result;
    }

    public static ResultVO error(Integer code, String Msg) {
        ResultVO result = new ResultVO();
        result.setCode(code);
        if (StringUtils.hasText(Msg)) {
            result.setMsg(Msg);
        } else {
            result.setMsg("失败");
        }
        return result;
    }

    public static ResultVO error() {
        return new ResultVO()
                .setCode(SysConstants.Result.ERROR.getCode())
                .setMsg(SysConstants.Result.ERROR.getMsg());
    }

    public static ResultVO error(String msg) {
        return new ResultVO()
                .setCode(SysConstants.Result.ERROR.getCode())
                .setMsg(msg);
    }
}
