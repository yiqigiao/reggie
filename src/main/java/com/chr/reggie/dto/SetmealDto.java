package com.chr.reggie.dto;

import com.chr.reggie.pojo.Setmeal;
import com.chr.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
