package io.github.xyz327.xauditor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 3:30 下午
 */
public interface XAuditorInfoProvider {
    /**
     * @param proceedingJoinPoint
     * @param methodSignature
     * @param xAuditor
     * @param httpRequest
     * @param principal
     */
    XAuditorInfo getXAuditorInfo(ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature, XAuditor xAuditor, HttpServletRequest httpRequest, Principal principal);
}
