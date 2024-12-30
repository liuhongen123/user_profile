package cn.temptation.web;

import cn.temptation.dao.RoleDao;
import cn.temptation.domain.Role;
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
import java.util.Map;

@Controller
public class RoleController {
    @Autowired
    private RoleDao roleDao;

    @RequestMapping("/role")
    public String index() {
        return "role";
    }

    @RequestMapping("/role_list")
    @ResponseBody
    public Map<String, Object> roleList(@RequestParam Map<String, Object> queryParams) {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<Role> specification = (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;

                if (keyword != null && !"".equals(keyword)) {
                    Path path = root.get("rolename");
                    predicate = cb.like(path, "%" + keyword + "%");
                }

                return predicate;
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "roleid");

            Page<Role> roles = roleDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", roles.getTotalElements());
            result.put("data", roles.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }

        return result;
    }

    @RequestMapping("/role_delete")
    @ResponseBody
    public Integer roleDelete(@RequestParam String roleid) {
        try {
            roleDao.deleteById(Integer.parseInt(roleid));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @RequestMapping("/role_view")
    public String view(Integer roleid, Model model) {
        Role role = new Role();
        if (roleid != null) {
            role = roleDao.getOne(roleid);
        }
        model.addAttribute("role", role);
        return "role_view";
    }

    @RequestMapping("/role_update")
    @ResponseBody
    public Integer roleUpdate(Role role) {
        try {
            roleDao.save(role);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}