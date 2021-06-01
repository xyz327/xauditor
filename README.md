# X-Auditor

> 基于 spring aop 对 spring mvc 的请求做拦截。输出审计信息(用户操作接口以及返回数据).

## spring-boot 使用

### 增加依赖

```xml
<dependency>
    <groupId>io.github.xyz327</groupId>
    <artifactId>xauditor-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```
### springboot配置
```yaml
spring:
    applicaiton:
        name: xaduitor-app
    profiles:
        active: test
xauditor:
    # 是否拦截@PostMapping 等注解
    controllerAdvice: true
    # 是否同步输出
    sync: true
```

## 基础使用

```java
@RestController
@RequestMapping()
public class TestController {
    
    @PostMapping("xauditor")
    @XAuditor
    public String test(String world) {
        return "hello" + world;
    }
}
```

当用户登陆后访问上面的接口将输出日志:  
```
2021-05-21 17:10:47.047  INFO 49855 --- [           main] i.g.x.x.impl.Slf4JXAuditorCreator        : xauditor: action: test. detail: 用户:xaduit测试用户 访问:io.github.xyz327.xaduitor.TestController#test 参数: [world]
```

## 拦截@PostMapping/@PutMapping 等 controller 注解

```java
@RestController
@RequestMapping()
public class TestController {

    @PostMapping("no-xaduitor")
    public String noXAduitor(String world) {
        return "hello" + world;
    }
}
```
当用户登陆后访问上面的接口将输出日志:  
```
2021-05-21 17:10:47.047  INFO 49855 --- [           main] i.g.x.x.impl.Slf4JXAuditorCreator        : xauditor: action: noXAduitor. detail: 用户:xaduit测试用户 访问:io.github.xyz327.xaduitor.TestController#noXAduitor 参数: [world]
```

## 自定义结果输出(日志,MQ,DB等)

> 实现`io.github.xyz327.xauditor.XAuditorCreator`
```java
@Configuration
public class XAduitorConfig{
    @Bean
    public XAuditorCreator xAuditorCreator() {
        return new XAuditorCreator() {
            @Override
            public void postProcess(XAuditorInfo xAuditorInfo) {
                System.out.println("成功后输出信息");
            }

            @Override
            public void throwProcess(XAuditorInfo xAuditorInfo, Throwable throwable) {
                System.out.println("异常后输出信息");
            }
        };
    }
}
```
## 异步输出

>使用 ` @XAuditor(sync = false)`
```java
@RestController
@RequestMapping()
public class TestController {
    @PostMapping("async-xauditor")
    @XAuditor(sync = false)
    public String asyncXAduitor(String world) {
        return "hello" + world;
    }
}
```
> 全局配置默认为异步输出
```yaml
xauditor:
    sync: false
```

### 自定义异步输出线程池  
> 实现`io.github.xyz327.xauditor.XAuditorExecutorProvider`
```java
@Configuration
public class XAduitorConfig{
   @Bean
   public XAuditorExecutorProvider xAuditorExecutorProvider(){
       return new XAuditorExecutorProvider() {
           @Override
           public Executor getExecutor() {
               return Executors.newSingleThreadExecutor();
           }
       };
   }
}
```


## 自定义用户信息解析(从 http 请求中获取当前用户)

> 实现`io.github.xyz327.xauditor.XAuditorPrincipalProvider`

```java
@Configuration
public class XAduitorConfig {
    @Bean
    public XAuditorPrincipalProvider xAuditorPrincipalProvider() {
        return new XAuditorPrincipalProvider() {

            @Override
            public Optional<Principal> getPrincipal(HttpServletRequest httpServletRequest) {
                return Optional.ofNullable(httpServletRequest.getUserPrincipal());
            }
        };
    }
}
```
## 自定义输出详情解析

> 输出时的日志内容,可以包含请求信息, 操作人信息, 返回结果等

> 实现 `io.github.xyz327.xauditor.XAuditorDetailResolver`

```java
@Configuration
public class XAduitorConfig {
    @Bean
    public XAuditorDetailResolver xAuditorDetailResolver() {
        return new XAuditorDetailResolver() {
            
            @Override
            public String resolve(XAuditor xAuditor, XAuditorInfo xAuditorInfo, Object returnObject) {
                return String.format("用户:%s 访问:%s#%s 参数: %s", xAuditorInfo.getUsername(), xAuditorInfo.getClassName(), xAuditorInfo.getMethodName()
                    , Arrays.asList(xAuditorInfo.getMethodParams()));
            }
        };
    }
}
```

## 自定义审计信息解析

> 实现`io.github.xyz327.xauditor.XAuditorInfoProvider`

```java
@Configuration
public class XAduitorConfig {
    @Bean
    public XAuditorInfoProvider xAuditorInfoProvider() {
        return new XAuditorInfoProvider() {

            @Override
            public XAuditorInfo getXAuditorInfo(ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature, XAuditor xAuditor, HttpServletRequest httpRequest, Principal principal) {
                return XAuditorInfo.builder().build();
            }
        };
    }
}
```
