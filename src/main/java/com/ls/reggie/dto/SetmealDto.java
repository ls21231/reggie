package com.ls.reggie.dto;


import com.ls.reggie.entity.Setmeal;
import com.ls.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
