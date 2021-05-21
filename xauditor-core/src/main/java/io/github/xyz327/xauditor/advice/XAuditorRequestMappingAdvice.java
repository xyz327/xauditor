package io.github.xyz327.xauditor.advice;

import io.github.xyz327.xauditor.XAuditor;
import io.github.xyz327.xauditor.XAuditorProcessor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * 拦截 controller 中被 @{@link RequestMapping}和 @{@link GetMapping} 等注解标记的方法
 * 如果方法上存在 @{@link XAuditor} 那就优先使用 XAuditor 注解
 *
 * @author xizhou
 * @since 2020/6/22 7:08 下午
 */
@Aspect
@RequiredArgsConstructor
public class XAuditorRequestMappingAdvice {
    private final XAuditorProcessor xAuditorProcessor;

    @Value("${xauditor.actionDetail:''}")
    private String actionDetail;

    @Around("!@annotation(io.github.xyz327.xauditor.XAuditor) && (" +
        "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
        "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
        "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
        "@annotation(org.springframework.web.bind.annotation.PatchMapping)" +
        ")"
    )
    public Object auditAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        XAuditor xAuditor = methodSignature.getMethod().getAnnotation(XAuditor.class);
        if (xAuditor == null) {
            // 手动合成 XAuditor 注解
            HashMap<String, Object> attrs = new HashMap<>(2, 1);
            attrs.put("action", methodSignature.getMethod().getName());
            attrs.put("actionDetail", actionDetail);
            xAuditor = AnnotationUtils.synthesizeAnnotation(attrs, XAuditor.class, methodSignature.getMethod());
        }
        return xAuditorProcessor.process(proceedingJoinPoint, xAuditor);
    }
}
