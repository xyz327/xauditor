package io.github.xyz327.xaduitor;

import io.github.xyz327.xauditor.XAuditorProcessor;
import io.github.xyz327.xauditor.advice.XAuditorAdvice;
import io.github.xyz327.xauditor.advice.XAuditorRequestMappingAdvice;
import io.github.xyz327.xauditor.impl.DefaultXAuditorInfoProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:32 下午
 */
@Configuration
@EnableConfigurationProperties(XAduitorConfigurationProperties.class)
public class XAduitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultXAuditorInfoProvider defaultXAuditorInfoProvider() {
        return new DefaultXAuditorInfoProvider();
    }

    @Bean
    public XAuditorProcessor xAuditorProcessor() {
        return new XAuditorProcessor();
    }

    @Bean
    public XAuditorAdvice xAuditorAdvice() {
        return new XAuditorAdvice(xAuditorProcessor());
    }

    @Bean
    @ConditionalOnProperty(prefix = "xaduitor", value = {"controllerAdvice", "controller-advice"}, havingValue = "true")
    public XAuditorRequestMappingAdvice xAuditorRequestMappingAdvice() {
        return new XAuditorRequestMappingAdvice(xAuditorProcessor());
    }
}
