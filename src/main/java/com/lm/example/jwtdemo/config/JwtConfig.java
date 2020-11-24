package com.lm.example.jwtdemo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 1.JWT的token，区分大小写
 */
@Slf4j
@ConfigurationProperties(prefix = "config.jwt")
@Component
@Getter
@Setter
public class JwtConfig {
    private String secret;
    private long expire;
    private String header;
    private List<String> ignore;

    /**
     * 生成token
     * @param subject
     * @return
     */
    public String createToken (String subject) {
        LocalDateTime nowDate = LocalDateTime.now();
//        Date nowDate = new Date();
        // 过期时间
        LocalDateTime expireDate = nowDate.plusSeconds(expire);
//        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(Date.from(nowDate.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireDate.atZone(ZoneId.systemDefault()).toInstant()))
                // 加密
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    /**
     * 获取token中注册信息
     * @param token
     * @return
     */
    public Claims getTokenClaim (String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
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
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }

    /**
     * 获取token失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }
    /**
     * 获取用户名从token中
     */
    public String getUsernameFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }
}
