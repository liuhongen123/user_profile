package cn.temptation.dao;

import cn.temptation.domain.Score;
import cn.temptation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query(value = "SELECT * FROM t_user WHERE username = ?1", nativeQuery = true)
    User findByUserName(String username);
}