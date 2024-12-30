package cn.temptation.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salesid")
    private Integer salesid;
    @Column(name = "salesname")
    private String salesname;
}