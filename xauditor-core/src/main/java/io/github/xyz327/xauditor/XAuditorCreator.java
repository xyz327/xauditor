package io.github.xyz327.xauditor;

/**
 * 根据注解拦截 创建审计日志
 *
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2020/8/1 6:58 下午
 */
public interface XAuditorCreator {

    /**
     * aop 方法执行之后
     *
     * @param xAuditorInfo 审计信息
     */
    void postProcess(XAuditorInfo xAuditorInfo);

    /**
     * aop 方法执行出现异常之后
     *
     * @param xAuditorInfo 审计信息
     * @param throwable    aop 方法执行发生的异常
     */
    void throwProcess(XAuditorInfo xAuditorInfo, Throwable throwable);

    /**
     * aop 方法执行的 finally 块执行
     *
     * @param xAuditorInfo 审计信息
     */
    default void finallyProcess(XAuditorInfo xAuditorInfo) {

    }

}
