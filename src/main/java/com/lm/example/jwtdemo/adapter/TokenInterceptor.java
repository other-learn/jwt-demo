package com.lm.example.jwtdemo.adapter;

import com.lm.example.jwtdemo.config.JwtConfig;
import com.lm.example.jwtdemo.util.JwtTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 3. 配置拦截器
 * @author meng-liang
 */
@Slf4j
@Component
public class TokenInterceptor implements AsyncHandlerInterceptor {
    @Resource
    private JwtConfig jwtConfig;
    /**
     * This implementation always returns {@code true}.
     * HandlerInterceptor 接口方法
     * 预处理回调方法，在业务处理器处理请求之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws SignatureException {
        /** 地址过滤 */
        AtomicBoolean flag = new AtomicBoolean(false);
        Optional.ofNullable(jwtConfig)
                .map(JwtConfig::getIgnore)
                .orElseThrow(()->new RuntimeException())
                .stream()
                .forEach(ignore -> {
                    log.info("uri : {}", request.getRequestURI());
                    if (request.getRequestURI().equals(ignore)){
                        flag.set(true);
                    }
                });
        if (flag.get()) {
            return true;
        }
        /** Token 验证 */
        String token = request.getHeader(jwtConfig.getHeader());
        if(!StringUtils.hasLength(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        if(!StringUtils.hasLength(token)){
            throw new SignatureException(jwtConfig.getHeader()+ "不能为空");
        }

        Claims claims;
        try{
            claims = JwtTool.getTokenClaim(token, jwtConfig);
            if(claims == null || JwtTool.isTokenExpired(claims.getExpiration())){
                throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
            }
        }catch (Exception e){
            throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
        }

        /**
         *  设置 identityId 用户身份ID(数据库ID)
         */
        request.setAttribute("identityId", claims.getSubject());

        log.debug("---------------------------解密的token信息----------------------------------");
        log.debug("ID: {}", claims.getId());
        log.debug("Subject: {}", claims.getSubject());
        log.debug("Issuer: {}", claims.getIssuer());
        log.debug("Expiration: {}", claims.getExpiration());
        return true;
    }

    /**
     * This implementation is empty.
     * HandlerInterceptor 接口方法
     * 后处理回调方法，实现处理器的后处理，在渲染视图之前执行
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     * This implementation is empty.
     * HandlerInterceptor 接口方法
     * 整个请求处理完成后的回调方法，在视图渲染完毕时执行，可用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                @Nullable Exception ex) throws Exception {
    }
}
