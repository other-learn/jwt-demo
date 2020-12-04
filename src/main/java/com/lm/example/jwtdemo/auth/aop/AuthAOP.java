package com.lm.example.jwtdemo.auth.aop;

import cn.hutool.core.convert.Convert;
import com.lm.example.jwtdemo.auth.annotation.HasAnyRole;
import com.lm.example.jwtdemo.auth.model.HasAnyRoleBean;
import com.lm.example.jwtdemo.config.JwtConfig;
import com.lm.example.jwtdemo.constants.AuthConstants;
import com.lm.example.jwtdemo.service.AuthService;
import com.lm.example.jwtdemo.util.JwtTool;
import com.lm.example.jwtdemo.util.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shijiang.luo
 * @RESOURCE 实现对 @CurrentDB 的切面处理
 * @createBy 2020/9/11
 */
@Slf4j
@Aspect
@Component
public class AuthAOP {
    @Resource
    private JwtConfig jwtConfig;
    @Resource
    private AuthService authService;

    @Pointcut("@annotation(com.lm.example.jwtdemo.auth.annotation.HasAnyRole)")
    public void dataSourcePointCut() {
    }


    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HasAnyRole hasAnyRole = getAnnotation(joinPoint);
        final AuthConstants.AuthorizationEnum[] auths = hasAnyRole.auth();
        final String[] roles = hasAnyRole.roles();
        log.debug("当前方法必须的角色为 : {}, 数据权限为 : {}", roles, auths);

        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        log.info("调用前连接点方法为 ： {}, 参数为：{}", methodName, args);

        // JWT中获取userID
        String userId = args.stream()
                .filter(arg -> arg instanceof HttpServletRequest)
                .map(arg -> Convert.convert(HttpServletRequest.class, arg))
                .map(arg -> JwtTool.getUsernameFromToken(arg.getHeader("token"), jwtConfig))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("没有 HttpServletRequest 的参数"));

        // 查询数据库中的权限
        Set<HasAnyRoleBean> dbHasAnyRoleBeans = authService.getRoleAndAuthByUserId(userId);

        // 角色
        List<String> dbRoles = dbHasAnyRoleBeans.stream()
                .map(HasAnyRoleBean::getRoles)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        AtomicBoolean needDo = new AtomicBoolean(true);

        Optional.of(Arrays.stream(roles)
                .filter(dbRole -> !"null".equals(dbRole)))
                .ifPresent(myStream->{
                    needDo.set(myStream.anyMatch(dbRoles::contains));
                });

        // 权限
        if (needDo.get()) {
            List<AuthConstants.AuthorizationEnum> dbAuths = dbHasAnyRoleBeans
                    .stream()
                    .map(HasAnyRoleBean::getAuth)
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toList());

            Stream<AuthConstants.AuthorizationEnum> enumStream = dbAuths.stream()
                    .filter(dbAuth -> !dbAuth.equals(AuthConstants.AuthorizationEnum.NULL));
            if (0L < enumStream.count()) {
                needDo.set(enumStream.anyMatch(dbRoles::contains));
            }
        }

        Object processObj = ResultTool.error();
        if (needDo.get()) {
            processObj = joinPoint.proceed();
        }
        return processObj;
    }

    /**
     * 获取方法 或 类上面的注释 @CurrentDB
     *
     * @param joinPoint
     * @return
     */
    private HasAnyRole getAnnotation(ProceedingJoinPoint joinPoint) {
//        Class<?> clazz = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(HasAnyRole.class);
    }
}
