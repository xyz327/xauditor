package io.github.xyz327.xaduitor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:35 下午
 */
@Data
@ConfigurationProperties(prefix = "xaduitor")
public class XAduitorConfigurationProperties {
    private boolean controllerAdvice;
    private String actionDetail;
}
