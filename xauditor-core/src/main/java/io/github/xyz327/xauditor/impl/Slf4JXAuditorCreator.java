package io.github.xyz327.xauditor.impl;

import io.github.xyz327.xauditor.XAuditorCreator;
import io.github.xyz327.xauditor.XAuditorInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:12 下午
 */
@Slf4j
public class Slf4JXAuditorCreator implements XAuditorCreator {

    @Override
    public void postProcess(XAuditorInfo xAuditorInfo) {
        log.info("xauditor: action: {}. detail: {}", xAuditorInfo.getAction(), xAuditorInfo.getActionDetail());
    }

    @Override
    public void throwProcess(XAuditorInfo xAuditorInfo, Throwable throwable) {

    }
}
