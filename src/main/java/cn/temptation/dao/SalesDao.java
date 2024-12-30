package cn.temptation.dao;

import cn.temptation.domain.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalesDao extends JpaRepository<Sales, Integer>, JpaSpecificationExecutor<Sales> {
}