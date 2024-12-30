package cn.temptation.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scoreid")
    private Integer scoreid;
    @Column(name = "score1")
    private Integer score1;
    @Column(name = "score2")
    private Integer score2;
    @Column(name = "score3")
    private Integer score3;
    @Column(name = "score4")
    private Integer score4;
    @Column(name = "score5")
    private Integer score5;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "salesid", foreignKey = @ForeignKey(name = "none"))
    private Sales sales;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "userid", foreignKey = @ForeignKey(name = "none"))
    private User user;
}