package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    //新增套餐和相应的菜品
    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void startOrStop(Integer status, Long id);

    void deleteBtach(List<Long> ids);

    SetmealVO getById(Long id);

    void update(SetmealDTO setmealDTO);
}
