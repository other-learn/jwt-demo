package com.lm.example.jwtdemo.controller;

import cn.hutool.json.JSONObject;
import com.lm.example.jwtdemo.config.JwtConfig;
import com.lm.example.jwtdemo.model.vo.ResultVO;
import com.lm.example.jwtdemo.util.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author meng-liang
 */
@Slf4j
@RestController
@RequestMapping("/jwt")
@CrossOrigin
public class TokenController {
    @Resource
    private JwtConfig jwtConfig ;

    /**
     * 用户登录接口
     * @param userName
     * @param passWord
     * @return
     */
    @PostMapping("/login")
    public ResultVO<?> login (@RequestParam("userName") String userName,
                              @RequestParam("passWord") String passWord){
        JSONObject json = new JSONObject();

        /** 验证userName，passWord和数据库中是否一致，如不一致，直接return ResultTool.errer(); 【这里省略该步骤】*/

        // 这里模拟通过用户名和密码，从数据库查询userId
        // 这里把userId转为String类型，实际开发中如果subject需要存userId，则可以JwtConfig的createToken方法的参数设置为Long类型
        String userId = 5 + "";
        String token = jwtConfig.createToken(userId) ;
        if (StringUtils.hasText(token)) {
            json.set("token", token) ;
        }
        return ResultTool.success(json) ;
    }

    /**
     * 需要 Token 验证的接口
     */
    @PostMapping("/info")
    public ResultVO<?> info (){
        return ResultTool.success("info") ;
    }

    /**
     * 根据请求头的token获取userId
     * @param request
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResultVO<?> getUserInfo(HttpServletRequest request){
        String usernameFromToken = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        return ResultTool.success(usernameFromToken) ;
    }
}
