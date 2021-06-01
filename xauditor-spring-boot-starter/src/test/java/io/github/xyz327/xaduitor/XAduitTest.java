package io.github.xyz327.xaduitor;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

/**
 * @author <a href="mailto:xyz327@outlook.com">xizhou</a>
 * @since 2021/5/21 4:39 下午
 */
@SpringBootTest(classes = XAduitTestApp.class)
@RunWith(SpringRunner.class)
//@WebMvcTest
public class XAduitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @SneakyThrows
    public void test() {
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "xaduit测试用户";
            }
        };
        MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext)

            .build()
            .perform(MockMvcRequestBuilders.post("/xaduitor").queryParam("world", "world").principal(principal))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void noXAduitor() {
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "xaduit测试用户";
            }
        };
        MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext)

            .build()
            .perform(MockMvcRequestBuilders.post("/no-xaduitor").queryParam("world", "world").principal(principal))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void asyncXAduitor() {
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "xaduit测试用户";
            }
        };
        MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext)

            .build()
            .perform(MockMvcRequestBuilders.post("/async-xaduitor").queryParam("world", "world").principal(principal))
            .andReturn();
    }
}
