package com.tuoke.security;

/*   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓　
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　┻　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┗━━━┓
 * 　　┃　  神兽保佑　┣┓
 * 　　┃　  永无BUG　 ┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛ 　　　
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试用例启动入口
 */
@SpringBootApplication
public class SecurityTestApplication {
    @RequestMapping("/")
    public String home(){
        return "index.html";
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityTestApplication.class, args);
    }
}
