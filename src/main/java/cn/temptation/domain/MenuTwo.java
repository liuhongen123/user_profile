package cn.temptation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 二级菜单
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTwo {
    private Integer menuid;
    private String menuname;
    private String menuurl;
}