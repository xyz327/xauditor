package io.github.xyz327.xaduitor;

import io.github.xyz327.xauditor.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:39 下午
 */
@SpringBootApplication
public class XAduitTestApp {
    public static void main(String[] args) {
        SpringApplication.run(XAduitTestApp.class, args);
    }


}
