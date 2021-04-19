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

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@SpringBootApplication
@Controller
@MapperScan("com.tuoke.security.dao")
public class SecurityApplication {
    /**
     * 启动服务，访问http://localhost，即可在页面上查看输入与输出
     * @return 前端index.html页面
     */
    @RequestMapping("/")
    public String home(){
        return "index.html";
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
