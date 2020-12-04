package com.lm.example.jwtdemo.auth.annotation;

import com.lm.example.jwtdemo.constants.AuthConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 角色注解
 * @author meng-liang
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Retention(RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface HasAnyRole {

    String[] roles() default {"null"};
    AuthConstants.AuthorizationEnum[] auth() default AuthConstants.AuthorizationEnum.NULL;
//    HasAnyRoleBean hasAnyRoleBean() default new HasAnyRoleBean();
}
