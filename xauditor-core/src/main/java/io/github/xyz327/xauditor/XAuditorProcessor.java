package io.github.xyz327.xauditor;

import io.github.xyz327.xauditor.impl.DefaultXAuditorDetailResolver;
import io.github.xyz327.xauditor.impl.DefaultXAuditorInfoProvider;
import io.github.xyz327.xauditor.impl.DefaultXAuditorPrincipalProvider;
import io.github.xyz327.xauditor.impl.Slf4JXAuditorCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2020/8/4 3:01 下午
 */
@Slf4j
@SuppressWarnings("unchecked")
@Component
public class XAuditorProcessor implements InitializingBean {

    @Setter(onMethod_ = {@Autowired(required = false)})
    private List<XAuditorCreator> xAuditorCreatorProvider = new ArrayList<>();
    @Setter(onMethod_ = {@Autowired(required = false)})
    private XAuditorDetailResolver xAuditorDetailResolver;
    @Setter(onMethod_ = {@Autowired(required = false)})
    private XAuditorPrincipalProvider xAuditorPrincipalProvider;
    @Setter(onMethod_ = {@Autowired(required = false)})
    private XAuditorInfoProvider xAuditorInfoProvider;

    /**
     * 处理 AOP 拦截，生成 audit 信息
     *
     * @param proceedingJoinPoint
     * @return
     */
    public Object process(ProceedingJoinPoint proceedingJoinPoint, XAuditor xAuditor) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        if (xAuditor.ignore()) {
            return proceedingJoinPoint.proceed(args);
        }
        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return proceedingJoinPoint.proceed(args);
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Optional<HttpServletRequest> httpRequestOptional = getHttpRequest(args);
        if (!httpRequestOptional.isPresent()) {
            return proceedingJoinPoint.proceed(args);
        }
        HttpServletRequest httpRequest = httpRequestOptional.get();
        Optional<Principal> principalOptional = xAuditorPrincipalProvider.getPrincipal(httpRequest);
        if (!principalOptional.isPresent()) {
            // 没有用户信息时 跳过
            return proceedingJoinPoint.proceed(args);
        }
        Principal principal = principalOptional.get();

        XAuditorInfo xAuditorInfo = xAuditorInfoProvider.getXAuditorInfo(proceedingJoinPoint, methodSignature, xAuditor, httpRequest, principal);


        try {
            Object proceed = proceedingJoinPoint.proceed(args);
            String actionDetail = xAuditorDetailResolver.resolve(xAuditor, xAuditorInfo, proceed);
            xAuditorInfo.setActionDetail(actionDetail);
            // before 没有异常的才执行 post
            triggerPostProcess(xAuditorInfo);
            return proceed;
        } catch (Throwable e) {
            triggerThrowProcess(xAuditorInfo, e);
            throw e;
        } finally {
            // finally
            triggerFinallyProcess(xAuditorInfo);
        }
    }

    private void triggerFinallyProcess(XAuditorInfo xAuditorInfo) {
        xAuditorCreatorProvider
            .forEach(xAuditorCreator -> {
                try {
                    xAuditorCreator.finallyProcess(xAuditorInfo);
                } catch (Exception e) {
                    log.warn("执行 audit finallyProcess 信息失败: {}", e.getMessage(), e);
                }
            });
    }

    private void triggerThrowProcess(XAuditorInfo xAuditorInfo, Throwable throwable) {
        xAuditorCreatorProvider
            .forEach(xAuditorCreator -> {
                try {
                    xAuditorCreator.throwProcess(xAuditorInfo, throwable);
                } catch (Exception e) {
                    log.warn("执行 audit throwProcess 信息失败: {}", e.getMessage(), e);
                }
            });
    }

    private void triggerPostProcess(XAuditorInfo xAuditorInfo) {
        xAuditorCreatorProvider
            .forEach(xAuditorCreator -> {
                try {
                    xAuditorCreator.postProcess(xAuditorInfo);
                } catch (Exception e) {
                    log.warn("执行 audit postProcess 信息失败: {}", e.getMessage(), e);
                }
            });
    }


    /**
     * 获取 {@link HttpServletRequest} 对象
     * 1. 从方法参数中获取
     * 2. 从{@link org.springframework.web.context.request.RequestContextHolder} 中获取
     *
     * @param args
     * @return HttpServletRequest
     */
    @NonNull
    private Optional<HttpServletRequest> getHttpRequest(Object[] args) {
        // 从方法参数中获取
        HttpServletRequest httpServletRequest = Arrays.stream(args)
            .filter(HttpServletRequest.class::isInstance)
            .map(HttpServletRequest.class::cast)
            .findFirst().orElse(null);

        if (httpServletRequest == null) {
            // 从 上下文中获取
            httpServletRequest = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest).orElse(null);
        }
        return Optional.ofNullable(httpServletRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (xAuditorDetailResolver == null) {
            xAuditorDetailResolver = new DefaultXAuditorDetailResolver();
        }
        if (xAuditorCreatorProvider.isEmpty()) {
            xAuditorCreatorProvider.add(new Slf4JXAuditorCreator());
        }
        if (xAuditorInfoProvider == null) {
            xAuditorInfoProvider = new DefaultXAuditorInfoProvider();
        }
        if (xAuditorPrincipalProvider == null) {
            xAuditorPrincipalProvider = new DefaultXAuditorPrincipalProvider();
        }
    }
}
