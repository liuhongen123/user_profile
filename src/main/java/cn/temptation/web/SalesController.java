package cn.temptation.web;

import cn.temptation.dao.SalesDao;
import cn.temptation.domain.Sales;
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
public class SalesController {
    @Autowired
    private SalesDao salesDao;

    @RequestMapping("/sales")
    public String index() {
        return "sales";
    }

    @RequestMapping("/sales_list")
    @ResponseBody
    public Map<String, Object> salesList(@RequestParam Map<String, Object> queryParams) {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer page = Integer.parseInt(queryParams.get("page").toString());
            Integer limit = Integer.parseInt(queryParams.get("limit").toString());
            String keyword = (String) queryParams.get("keyword");

            // 创建查询规格对象
            Specification<Sales> specification = (Root<Sales> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                Predicate predicate = null;

                if (keyword != null && !"".equals(keyword)) {
                    Path path = root.get("salesname");
                    predicate = cb.like(path, "%" + keyword + "%");
                }

                return predicate;
            };

            Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "salesid");

            Page<Sales> saleses = salesDao.findAll(specification, pageable);

            result.put("code", 0);
            result.put("msg", "查询OK");
            result.put("count", saleses.getTotalElements());
            result.put("data", saleses.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", "服务器内部错误");
            result.put("count", 0);
            result.put("data", new ArrayList());
        }

        return result;
    }

    @RequestMapping("/sales_delete")
    @ResponseBody
    public Integer salesDelete(@RequestParam String salesid) {
        try {
            salesDao.deleteById(Integer.parseInt(salesid));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @RequestMapping("/sales_view")
    public String view(Integer salesid, Model model) {
        Sales sales = new Sales();
        if (salesid != null) {
            sales = salesDao.getOne(salesid);
        }
        model.addAttribute("sales", sales);
        return "sales_view";
    }

    @RequestMapping("/sales_update")
    @ResponseBody
    public Integer salesUpdate(Sales sales) {
        try {
            salesDao.save(sales);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}