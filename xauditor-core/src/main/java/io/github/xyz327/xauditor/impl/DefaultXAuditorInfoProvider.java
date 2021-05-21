package io.github.xyz327.xauditor.impl;

import io.github.xyz327.xauditor.XAuditor;
import io.github.xyz327.xauditor.XAuditorInfo;
import io.github.xyz327.xauditor.XAuditorInfoProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 3:41 下午
 */
public class DefaultXAuditorInfoProvider implements XAuditorInfoProvider, EnvironmentAware {
    private static final String UNKNOWN_IP = "unknown";
    private String profiles;
    private String applicationName;

    @Override
    public XAuditorInfo getXAuditorInfo(ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature, XAuditor xAuditor, HttpServletRequest httpRequest, Principal principal) {
        XAuditorInfo.XAuditorInfoBuilder auditInfoBuilder = XAuditorInfo.builder();
        Method method = methodSignature.getMethod();
        String targetMethodName = methodSignature.getMethod().getName();
        Object[] args = proceedingJoinPoint.getArgs();
        // basic
        auditInfoBuilder.project(applicationName);
        auditInfoBuilder.env(profiles);
        auditInfoBuilder.className(AopUtils.getTargetClass(proceedingJoinPoint.getTarget()).getName());
        auditInfoBuilder.methodName(targetMethodName);
        auditInfoBuilder.methodParams(args);

        // request info
        String url = httpRequest.getRequestURI();
        auditInfoBuilder.url(url);
        String action = Optional.of(xAuditor.action()).filter(val -> !StringUtils.isEmpty(val)).orElse(targetMethodName);
        auditInfoBuilder.action(action);
        String ip = getRemoteIp(httpRequest);
        auditInfoBuilder.ip(ip);

        auditInfoBuilder.actionTime(LocalDateTime.now());

        auditInfoBuilder.username(principal.getName());

        return auditInfoBuilder.build();
    }

    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    @Override
    public void setEnvironment(Environment environment) {
        applicationName = environment.getProperty("spring.application.name");
        profiles = environment.getProperty("spring.profiles.active");
    }
}
