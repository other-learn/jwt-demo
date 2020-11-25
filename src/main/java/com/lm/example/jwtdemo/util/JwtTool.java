package com.lm.example.jwtdemo.util;

import com.lm.example.jwtdemo.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author meng-liang
 */
@Slf4j
public class JwtTool {
    /**
     * 生成token
     * @param subject
     * @return
     */
    public static String createToken (String subject, JwtConfig jwtConfig) {
        LocalDateTime nowDate = LocalDateTime.now();
//        Date nowDate = new Date();
        // 过期时间
        LocalDateTime expireDate = nowDate.plusSeconds(jwtConfig.getExpire());
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(subject)
                .setIssuedAt(Date.from(nowDate.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireDate.atZone(ZoneId.systemDefault()).toInstant()))
                // 加密
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }
    /**
     * 获取token中注册信息
     * @param token
     * @return
     */
    public static Claims getTokenClaim (String token, JwtConfig jwtConfig) {
        try {
            return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
        }catch (Exception e){
            log.error(" error : {}", e.getMessage());
            return null;
        }
    }
    /**
     * 验证token是否过期失效
     * @param expirationTime
     * @return
     */
    public static boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }

    /**
     * 获取token失效时间
     * @param token
     * @return
     */
    public static Date getExpirationDateFromToken(String token, JwtConfig jwtConfig) {
        return getTokenClaim(token, jwtConfig).getExpiration();
    }
    /**
     * 获取用户名从token中
     */
    public static String getUsernameFromToken(String token, JwtConfig jwtConfig) {
        return getTokenClaim(token, jwtConfig).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAtDateFromToken(String token, JwtConfig jwtConfig) {
        return getTokenClaim(token, jwtConfig).getIssuedAt();
    }
}
