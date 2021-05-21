package io.github.xyz327.xauditor.impl;

import io.github.xyz327.xauditor.XAuditorPrincipalProvider;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:00 下午
 */
public class DefaultXAuditorPrincipalProvider implements XAuditorPrincipalProvider {
    @Override
    public Optional<Principal> getPrincipal(HttpServletRequest httpServletRequest) {
        return Optional.ofNullable(httpServletRequest.getUserPrincipal());
    }
}
