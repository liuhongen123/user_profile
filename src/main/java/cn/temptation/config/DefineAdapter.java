package cn.temptation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DefineAdapter implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    // 登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 排除不被拦截的资源
        List<String> excludePaths = new ArrayList<>();
        // layui的静态文件
        excludePaths.add("/layui/**");

        // 静态页面
        excludePaths.add("/**/*.html");
        excludePaths.add("/css/*.css");
        excludePaths.add("/images/*.png");

        // 登录：以 / 或 /login 均可访问
        excludePaths.add("/");
        excludePaths.add("/login");
        excludePaths.add("/loginHandle");
//        excludePaths.add("/error");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludePaths);
    }
}