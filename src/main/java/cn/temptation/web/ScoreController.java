package cn.temptation.web;

import cn.temptation.dao.SalesDao;
import cn.temptation.dao.ScoreDao;
import cn.temptation.domain.Sales;
import cn.temptation.domain.Score;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScoreController {
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private SalesDao salesDao;

    @RequestMapping("/score")
    public String index() {
        return "score";
    }

    @RequestMapping("/score_list")
    @ResponseBody
    public Map<String, Object> scoreList(@RequestParam Map<String, Object> queryParams, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String condition = (String) queryParams.get("condition");
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<Score> specification = (Root<Score> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;
                Path path = null;

                // 从session中取出当前用户信息，获取该用户创建的数据
                if (null != user) {
                    path = root.join("user", JoinType.INNER);
                    predicate = cb.equal(path.get("userid"), user.getUserid());
                }

                if (condition != null && !"".equals(condition) && keyword != null && !"".equals(keyword)) {
                    switch (condition) {
                        case "salesname":    // 销售名称
                            path = root.join("sales", JoinType.INNER);
                            predicate = cb.like(path.get("salesname"), "%" + keyword + "%");
                            break;
                    }
                }

                return predicate;
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "scoreid");

            Page<Score> scores = scoreDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", scores.getTotalElements());
            result.put("data", scores.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }

        return result;
    }

    @RequestMapping("/score_delete")
    @ResponseBody
    public Integer scoreDelete(@RequestParam String scoreid) {
        try {
            scoreDao.deleteById(Integer.parseInt(scoreid));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @RequestMapping("/score_view")
    public String view(Integer scoreid, Model model) {
        Score score = new Score();
        if (scoreid != null) {
            score = scoreDao.findByScoreid(scoreid);
        }
        model.addAttribute("score", score);
        return "score_view";
    }

    @RequestMapping("/sales_load")
    @ResponseBody
    public List<Sales> salesList() {
        return salesDao.findAll();
    }

    @RequestMapping("/score_update")
    @ResponseBody
    public Integer scoreUpdate(Score score, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (null != user) {
                score.setUser(user);
            }

            scoreDao.save(score);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}