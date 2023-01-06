package com.chr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chr.reggie.common.R;
import com.chr.reggie.pojo.Category;
import com.chr.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        Page<Category> pages = new Page<>(page, pageSize);
        categoryService.page(pages, queryWrapper);
        return R.success(pages);
    }
    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        boolean save = categoryService.save(category);
        return save ? R.success("新增成功") : R.error("操作异常");
    }
    @DeleteMapping
    public R<String> deleteCategory(Long ids) {
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        boolean b = categoryService.updateById(category);
        return b ? R.success("修改成功") : R.error("操作异常");
    }
    @GetMapping("/list")
    public R<List<Category>> list(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.select(Category::getId,Category::getName).eq(Category::getType, type);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
