package io.github.xyz327.xauditor.impl;

import io.github.xyz327.xauditor.XAuditor;
import io.github.xyz327.xauditor.XAuditorDetailResolver;
import io.github.xyz327.xauditor.XAuditorInfo;

import java.util.Arrays;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:54 下午
 */
public class DefaultXAuditorDetailResolver implements XAuditorDetailResolver {
    @Override
    public String resolve(XAuditor xAuditor, XAuditorInfo xAuditorInfo, Object returnObject) {
        return String.format("用户:%s 访问:%s#%s 参数: %s", xAuditorInfo.getUsername(), xAuditorInfo.getClassName(), xAuditorInfo.getMethodName()
            , Arrays.asList(xAuditorInfo.getMethodParams()));
    }
}
