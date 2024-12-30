package cn.temptation.dao;

import cn.temptation.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreDao extends JpaRepository<Score, Integer>, JpaSpecificationExecutor<Score> {
    @Query(value = "SELECT * FROM t_score WHERE scoreid = ?1", nativeQuery = true)
    Score findByScoreid(Integer scoreid);

    @Query(value = "SELECT sc.salesid, s.`salesname`, SUM(score1 + score2 + score3 + score4 + score5) / COUNT(sc.`userid`) AS avgscore\n" +
            "FROM t_score AS sc INNER JOIN t_sales AS s ON sc.`salesid` = s.`salesid`\n" +
            "GROUP BY sc.salesid\n" +
            "ORDER BY avgscore DESC", nativeQuery = true)
    List<Object> scoreAllSales();

    @Query(value = "SELECT sc.salesid, s.`salesname`\n" +
            ", SUM(score1) / COUNT(sc.`userid`) AS `仪容仪表`\n" +
            ", SUM(score2) / COUNT(sc.`userid`) AS `沟通表达`\n" +
            ", SUM(score3) / COUNT(sc.`userid`) AS `临场应变`\n" +
            ", SUM(score4) / COUNT(sc.`userid`) AS `技术知识`\n" +
            ", SUM(score5) / COUNT(sc.`userid`) AS `组织协调`\n" +
            "FROM t_score AS sc INNER JOIN t_sales AS s ON sc.`salesid` = s.`salesid`\n" +
            "GROUP BY sc.salesid\n" +
            "HAVING s.salesname = ?1", nativeQuery = true)
    List<Object> scoreBySalesname(String salesname);
}