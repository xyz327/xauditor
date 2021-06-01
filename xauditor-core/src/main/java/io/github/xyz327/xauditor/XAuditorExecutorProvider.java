package io.github.xyz327.xauditor;

import java.util.concurrent.Executor;

/**
 * @author xizhou
 * @date 2021/6/1 20:42
 */
public interface XAuditorExecutorProvider {

    Executor getExecutor();
}
