package io.github.xyz327.xauditor;

/**
 * @author xizhou
 * @since 2020/6/22 9:53 下午
 */
public interface XAuditorDetailResolver {

    /**
     * 根据action解析出action详情
     *
     * @param xAuditor
     * @param xAuditorInfo
     * @param returnObject 返回值
     * @return 审计信息
     */
    String resolve(XAuditor xAuditor, XAuditorInfo xAuditorInfo, Object returnObject);

}
