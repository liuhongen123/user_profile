package cn.temptation.web;

import cn.temptation.dao.RoleDao;
import cn.temptation.dao.UserDao;
import cn.temptation.domain.Role;
import cn.temptation.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @RequestMapping("/user")
    public String index() {
        return "user";
    }

    @RequestMapping("/user_list")
    @ResponseBody
    public Map<String, Object> userList(@RequestParam Map<String, Object> queryParams) {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String condition = (String) queryParams.get("condition");
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<User> specification = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;
                Path path = null;

                if (condition != null && !"".equals(condition) && keyword != null && !"".equals(keyword)) {
                    switch (condition) {
                        case "username":    // 用户名称
                            path = root.get("username");
                            predicate = cb.like(path, "%" + keyword + "%");
                            break;
                        case "rolename":    // 角色名称
                            path = root.join("role", JoinType.INNER);
                            predicate = cb.like(path.get("rolename"), "%" + keyword + "%");
                            break;
                    }
                }

                return predicate;
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "userid");

            Page<User> users = userDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", users.getTotalElements());
            result.put("data", users.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }

        return result;
    }

    @RequestMapping("/user_delete")
    @ResponseBody
    public Integer userDelete(@RequestParam String userid) {
        try {
            userDao.deleteById(Integer.parseInt(userid));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @RequestMapping("/user_view")
    public String view(Integer userid, Model model) {
        User user = new User();
        if (userid != null) {
            user = userDao.getOne(userid);
        }
        model.addAttribute("user", user);
        return "user_view";
    }

    @RequestMapping("/role_load")
    @ResponseBody
    public List<Role> roleList() {
        return roleDao.findAll();
    }

    @RequestMapping("/user_update")
    @ResponseBody
    public Integer userUpdate(User user) {
        try {
            userDao.save(user);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}