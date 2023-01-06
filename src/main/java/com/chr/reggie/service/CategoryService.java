package com.chr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chr.reggie.exception.CustomException;
import com.chr.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long ids) throws CustomException;
}
