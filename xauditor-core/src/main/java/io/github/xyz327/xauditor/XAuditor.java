package io.github.xyz327.xauditor;
import java.lang.annotation.*;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 2:36 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XAuditor {
    /**
     * 操作名  默认为方法名
     *
     * @return
     */
    String action() default "";

    /**
     * 操作说明详情
     *
     * @return
     */
    String actionDetail() default "";

    /**
     * 是否同步处理
     * @return
     */
    boolean sync() default true;

    /**
     * 是否使用模板解析 actionDetail
     */
    boolean useTemplateExpression() default true;

    /**
     * 是否忽略, 不进行生成审计日志操作
     *
     * @return
     */
    boolean ignore() default false;
}
