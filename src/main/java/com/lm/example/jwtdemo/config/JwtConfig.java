package com.lm.example.jwtdemo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 1.JWT的token，区分大小写
 * @author meng-liang
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
}
