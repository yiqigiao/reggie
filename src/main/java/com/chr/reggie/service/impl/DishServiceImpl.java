package com.chr.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chr.reggie.common.R;
import com.chr.reggie.dto.DishDto;
import com.chr.reggie.mapper.DishMapper;
import com.chr.reggie.pojo.Category;
import com.chr.reggie.pojo.Dish;
import com.chr.reggie.pojo.DishFlavor;
import com.chr.reggie.service.DishFlavorService;
import com.chr.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void addDish(DishDto dish) {
        // 添加菜品
        this.save(dish);
        // 添加菜品的口味
        List<DishFlavor> flavors = dish.getFlavors();
        if (flavors.size() != 0) {
            // 选择了口味
            for (DishFlavor dishFlavor : flavors) {
                DishFlavor flavor = new DishFlavor();
                flavor.setDishId(dish.getId());
                flavor.setName(dishFlavor.getName());
                flavor.setValue(dishFlavor.getValue());
                dishFlavorService.save(flavor);
            }
        }
    }

    @Override
    public DishDto getByIdWithFlavorAndCategory(Long id) {
        // 查询菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询菜品对应的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavorList = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dish) {
        // 修改菜品基本信息
        this.updateById(dish);
        // 删除原来的口味，直接重新加一条
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dish.getFlavors();
        if (flavors.size() != 0) {
            // 选择了口味
            for (DishFlavor dishFlavor : flavors) {
                DishFlavor flavor = new DishFlavor();
                flavor.setDishId(dish.getId());
                flavor.setName(dishFlavor.getName());
                flavor.setValue(dishFlavor.getValue());
                dishFlavorService.save(flavor);
            }
        }
    }

    @Override
    public void deleteWithFlavor(String ids) {
        // 先删除菜品对应的口味，再删除菜品基本信息
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, list);
        dishFlavorService.remove(queryWrapper);
        this.removeByIds(list);
    }
}
