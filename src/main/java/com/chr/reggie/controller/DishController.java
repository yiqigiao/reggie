package com.chr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chr.reggie.common.R;
import com.chr.reggie.dto.DishDto;
import com.chr.reggie.pojo.Category;
import com.chr.reggie.pojo.Dish;
import com.chr.reggie.service.CategoryService;
import com.chr.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dish) {
        dishService.addDish(dish);
        return R.success("添加菜品成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //DishDto是前端要的东西和后端的Dish不一样，要扩展一下
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝，忽略record对象，因为record就是查出来的记录数，也就是pageInfo
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        //将List集合搬入Dto中
        //这里是流式编程的内容，或者用foreach来进行搬运也可以解决
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavorAndCategory(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dish) {
        dishService.updateWithFlavor(dish);
        return R.success("修改成功");
    }
    @PostMapping("/status/0")
    public R<String> status0(String ids) {
        String[] split = ids.split(",");
        for (String s : split) {
            Dish dish = dishService.getById(s);
            dish.setStatus(0);
            dishService.updateById(dish);
        }
        return R.success("停用成功");
    }
    @PostMapping("/status/1")
    public R<String> status1(String ids) {
        String[] split = ids.split(",");
        for (String s : split) {
            Dish dish = dishService.getById(s);
            dish.setStatus(1);
            dishService.updateById(dish);
        }
        return R.success("启用成功！");
    }
    @DeleteMapping
    public R<String> delete(String ids) {
        dishService.deleteWithFlavor(ids);

        return R.success("删除成功！");
    }
    @GetMapping("/list")
    public R<List<Dish>> getDishsByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        queryWrapper.select(Dish::getId,Dish::getName, Dish::getStatus, Dish::getPrice);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);

    }

}