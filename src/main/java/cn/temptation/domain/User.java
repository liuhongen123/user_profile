package cn.temptation.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userid;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "roleid", foreignKey = @ForeignKey(name = "none"))
    private Role role;
    @Transient // 忽略不生成该注解的字段
    private List<MenuOne> menuOnes;
}