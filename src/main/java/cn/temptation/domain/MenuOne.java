package cn.temptation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 一级菜单
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOne {
    private String menuname;
    private List<MenuTwo> menuTwos;
}