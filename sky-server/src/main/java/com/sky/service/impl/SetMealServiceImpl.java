package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//告诉Spring这里是负责服务层的代码。
@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    //由于涉及到两张表的操作，我们在方法前面加一个@Transactional事务注解
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //向套餐插入数据
        setmealMapper.insert(setmeal);
        Long setMealId=setmeal.getId();
        //向菜品插入n条数据
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();

        if(setmealDishes!=null&&setmealDishes.size()>0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setMealId);
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void deleteBtach(List<Long> ids) {
        //判断当前套餐是否在启售中，启售中不能删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删除套餐表里的套餐
        setmealMapper.deleteByIds(ids);

        //删除套餐菜品表里的菜品
        setMealDishMapper.deleteByDishIds(ids);

    }

    @Override
    public SetmealVO getById(Long id) {
        //根据id查询套餐
        Setmeal setmeal = setmealMapper.getById(id);

        //根据id查询对应菜品

        List<SetmealDish> setmealDishs = setMealDishMapper.getBySetmealId(id);

        //将结果封装到VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishs);

        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //修改套餐
        setmealMapper.update(setmeal);

        List<Long> ids = new ArrayList<>(); //存放套餐id的集合，这样可以不用新定义一个根据单个id查询的方法
        ids.add(setmealDTO.getId());

        //删除原有的菜品
        setMealDishMapper.deleteByDishIds(ids);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        //插入套餐对应的菜品

        if (setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
        }

        //批量插入菜品
        setMealDishMapper.insertBatch(setmealDishes);
    }
}
