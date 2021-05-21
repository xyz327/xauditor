package io.github.xyz327.xauditor.advice;

import io.github.xyz327.xauditor.XAuditor;
import io.github.xyz327.xauditor.XAuditorProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 记录操作日志 在controller的方法上使用 <code>@XAuditor</code>
 *
 * @author xizhou
 * @since 2020/6/22 7:08 下午
 */
@Aspect
@RequiredArgsConstructor
@Component
public class XAuditorAdvice {
    private final XAuditorProcessor xAuditorProcessor;

    @Around("@annotation(io.github.xyz327.xauditor.XAuditor)")
    public Object auditAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        XAuditor xAuditor = methodSignature.getMethod().getAnnotation(XAuditor.class);
        return xAuditorProcessor.process(proceedingJoinPoint, xAuditor);
    }


}
