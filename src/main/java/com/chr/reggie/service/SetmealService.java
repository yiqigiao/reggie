package com.chr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chr.reggie.dto.SetmealDto;
import com.chr.reggie.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    void insertSetmeal(SetmealDto setmealDto);

    void updateSetmeal(SetmealDto setmealDto);
}
