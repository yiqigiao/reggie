package com.chr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chr.reggie.common.R;
import com.chr.reggie.dto.SetmealDto;
import com.chr.reggie.pojo.Category;
import com.chr.reggie.pojo.DishFlavor;
import com.chr.reggie.pojo.Setmeal;
import com.chr.reggie.pojo.SetmealDish;
import com.chr.reggie.service.CategoryService;
import com.chr.reggie.service.SetmealDishService;
import com.chr.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pages = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDto = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName, name);
        queryWrapper.eq(Setmeal::getIsDeleted, 0);
        setmealService.page(pages, queryWrapper);
        BeanUtils.copyProperties(pages, setmealDto,"records");
        List<Setmeal> records = pages.getRecords();
        List<SetmealDto> list = new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto1 = new SetmealDto();
            BeanUtils.copyProperties(record,setmealDto1);
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto1.setCategoryName(category.getName());
            }
            list.add(setmealDto1);
        }
        setmealDto.setRecords(list);
        return R.success(setmealDto);
    }
    @PostMapping
    public R<String> insertSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.insertSetmeal(setmealDto);
        return R.success("新增成功");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);
        Category category = categoryService.getById(setmeal.getCategoryId());
        setmealDto.setCategoryName(category.getName());
        return R.success(setmealDto);
    }
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmeal(setmealDto);
        return R.success("更新成功");
    }
    @PostMapping("/status/0")
    public R<String> changeStatusTo0(String ids) {
        // 分隔
        String[] split = ids.split(",");
        for (String s : split) {
            long id = Long.parseLong(s);
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(0);
            setmealService.updateById(setmeal);
        }
        return R.success("停售成功！");
    }
    @PostMapping("/status/1")
    public R<String> changeStatusTo1(String ids) {
        // 分隔
        String[] split = ids.split(",");
        for (String s : split) {
            long id = Long.parseLong(s);
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(1);
            setmealService.updateById(setmeal);
        }
        return R.success("停售成功！");
    }
    @DeleteMapping
    public R<String> deleteById(String ids) {
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        List<Setmeal> list = setmealService.listByIds(idList);
        for (Setmeal setmeal : list) {
            setmeal.setIsDeleted(1);
        }
        setmealService.updateBatchById(list);
        return R.success("成功");
    }
}
