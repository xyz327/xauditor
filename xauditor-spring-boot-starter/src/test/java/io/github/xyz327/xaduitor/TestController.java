package io.github.xyz327.xaduitor;

import io.github.xyz327.xauditor.XAuditor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:39 下午
 */
@RestController
@RequestMapping()
public class TestController {

    @PostMapping("xauditor")
    @XAuditor
    public String test(String world) {
        return "hello" + world;
    }

    @PostMapping("no-xauditor")
    public String noXAduitor(String world) {
        return "hello" + world;
    }

    @PostMapping("async-xauditor")
    @XAuditor(sync = false)
    public String asyncXAduitor(String world) {
        return "hello" + world;
    }
}
