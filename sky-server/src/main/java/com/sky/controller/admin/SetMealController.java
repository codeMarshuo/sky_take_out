package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    //接受前端请求
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}",setmealDTO);
        setMealService.saveWithDish(setmealDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}",setmealPageQueryDTO);

        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);

        log.info("分页查询结果：{}",pageResult);

        return Result.success(pageResult);
    }
    @PostMapping("/status/{status}")
    @ApiOperation("启售、停售套餐")
    public Result startOrStop(@PathVariable Integer status,Long id) {
        log.info("启售、停售套餐：{},{}",status,id);

        setMealService.startOrStop(status,id);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("根据id批量删除套餐：{}",ids);

        setMealService.deleteBtach(ids);

        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐")
    public Result<SetmealVO> getBySetmealId(@PathVariable Long id) {
        log.info("根据id查询套餐：{}",id);

        SetmealVO setmealVOs = setMealService.getById(id);

        return Result.success(setmealVOs);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);

        setMealService.update(setmealDTO);

        return Result.success();
    }
}

