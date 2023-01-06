package com.chr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chr.reggie.common.R;
import com.chr.reggie.dto.DishDto;
import com.chr.reggie.pojo.Dish;

public interface DishService extends IService<Dish> {
    void addDish(DishDto dish);
    DishDto getByIdWithFlavorAndCategory(Long id);

    void updateWithFlavor(DishDto dish);

    void deleteWithFlavor(String ids);
}
