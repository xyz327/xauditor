package io.github.xyz327.xauditor;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审计信息
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2020/8/2 10:29 上午
 */
@Data
@Builder
public class XAuditorInfo implements Serializable {
    private final String project;
    private final String env;
    private final String className;
    private final String methodName;
    private final Object[] methodParams;
    /**
     * 操作名
     */
    private final String action;
    /**
     * 操作详情, 只有在方法执行完成后才会有值
     */
    @Nullable
    private String actionDetail;
    /**
     * 操作时间
     */
    private final LocalDateTime actionTime;
    /**
     * spring {@link org.springframework.web.bind.annotation.RequestMapping} 标记的值。可能为空
     */
    @Nullable
    private final String mappingUrl;
    private final String url;
    private final String ip;
    private final String userId;
    private final String username;
}
