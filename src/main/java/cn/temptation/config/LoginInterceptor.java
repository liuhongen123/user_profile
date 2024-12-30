package cn.temptation.config;

import cn.temptation.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("---------------拦截器"+user);
        // 未登录去登录
        if (user == null) {
            // 注意：这里需要写上"/login"，而不是"login"，否则重定向的地址只会替换最后一个"/"后面的字符串
            response.sendRedirect("/login");
        }
        return true;
    }
}