package io.github.xyz327.xauditor;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2020/8/5 2:08 下午
 */
public interface XAuditorPrincipalProvider {
    /**
     * 获取用户信息
     *
     * @param httpServletRequest request
     * @return
     */
    Optional<Principal> getPrincipal(HttpServletRequest httpServletRequest);
}
