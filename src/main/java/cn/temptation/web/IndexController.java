package cn.temptation.web;

import cn.temptation.dao.UserDao;
import cn.temptation.domain.MenuOne;
import cn.temptation.domain.MenuTwo;
import cn.temptation.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserDao userDao;

    /**
     * 设置默认打开地址http://localhost的跳转(需要在拦截器中排除)
     * 1.已登录，跳转到index.html，把user返回前端渲染
     * 2.未登录，跳转到登录页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (null != user) {
            modelMap.addAttribute("user", user);
            return "index";
        } else {
            return "login";
        }
    }

    /**
     * 设置默认打开地址http://localhost的跳转(需要在拦截器中排除)
     * 1.已登录，跳转到index.html，把user返回前端渲染
     * 2.未登录，跳转到登录页
     */
    @GetMapping(value = "/login")
    public String loginView(HttpServletRequest request, ModelMap modelMap) {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        if (null != user) {
//            modelMap.addAttribute("user", user);
//            return "index";
//        } else {
        return "login";
//        }
    }

    /**
     * 登录(需要在拦截器中排除)
     * 1.已登录，跳转到index.html，把user返回前端渲染
     * 2.未登录，验证密码，如果密码正确，跳转到index.html, 则把用户信息放入session，并把user返回前端渲染
     * 如果密码错误，跳转到login.html
     */
    @PostMapping("/loginHandle")
    public String login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password,
                        HttpServletRequest request,
                        ModelMap modelMap) {
        // 先验证session，再验证密码
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (null != user) {
            modelMap.addAttribute("user", user);
            return "index";
        } else {
            // 验证密码
            User tempUser = userDao.findByUserName(username);
            System.out.println(password + "-------" + tempUser.getPassword());
            System.out.println(tempUser.getPassword().equals(password));
            if (tempUser != null && tempUser.getPassword().equals(password)) {
                // 编辑菜单
                List<MenuTwo> listTwo1 = new ArrayList<>();
                List<MenuTwo> listTwo2 = new ArrayList<>();
                List<MenuOne> listOne = new ArrayList<>();

                switch (tempUser.getRole().getRolename()) {
                    case "管理员":
                        // 业务模块下的子菜单
                        listTwo1.add(new MenuTwo(4, "评分结果", "result"));
                        listTwo1.add(new MenuTwo(5, "评分管理", "score"));
                        // 基础模块下的子菜单
                        listTwo2.add(new MenuTwo(1, "用户管理", "user"));
                        listTwo2.add(new MenuTwo(2, "角色管理", "role"));
                        listTwo2.add(new MenuTwo(3, "销售管理", "sales"));

                        listOne.add(new MenuOne("业务模块", listTwo1));
                        listOne.add(new MenuOne("基础模块", listTwo2));
                        break;
                    case "评委":
                        // 业务模块下的子菜单
                        listTwo1.add(new MenuTwo(4, "评分结果", "result"));
                        listTwo1.add(new MenuTwo(5, "评分管理", "score"));

                        listOne.add(new MenuOne("业务模块", listTwo1));
                        break;
                    case "销售":
                        // 业务模块下的子菜单
                        listTwo1.add(new MenuTwo(4, "评分结果", "result"));

                        listOne.add(new MenuOne("业务模块", listTwo1));
                        break;
                    default:
                        break;
                }

                tempUser.setMenuOnes(listOne);

                // 用户信息放入session
                request.getSession().setAttribute("user", tempUser);
                modelMap.addAttribute("user", tempUser);
                System.out.println("-----------------------------");
                return "index";
            } else {
                System.out.println("2----------------------------");
                return "login";
            }
        }
    }

    /**
     * 注销退出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:login";
    }

}