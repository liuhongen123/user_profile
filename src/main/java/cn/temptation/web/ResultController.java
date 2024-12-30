package cn.temptation.web;

import cn.temptation.dao.ScoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ResultController {
    @Autowired
    private ScoreDao scoreDao;

    @RequestMapping("/result")
    public String index() {
        return "result";
    }

    @RequestMapping("/scoreAllSales")
    @ResponseBody
    public List<Object> scoreAllSales() {
        return scoreDao.scoreAllSales();
    }

    @RequestMapping("/scoreSales")
    @ResponseBody
    public List<Object> scoreSales(String salesname) {
        return scoreDao.scoreBySalesname(salesname);
    }
}