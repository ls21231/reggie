package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.common.CustomerException;
import com.ls.reggie.dto.SetmealDto;
import com.ls.reggie.entity.Setmeal;
import com.ls.reggie.entity.SetmealDish;
import com.ls.reggie.mapper.SetmealMapper;
import com.ls.reggie.service.SetmealDishService;
import com.ls.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 添加套餐的基本数据
        this.save(setmealDto);

        // 添加套餐的菜品数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 批量插入套餐菜品数据
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);

        // 说明有套餐正在售卖中，抛出业务异常
        if(count > 0){
            throw new CustomerException("套餐正在售卖中，不能删除");
        }

        // 先删除套餐表里的数据
        this.removeByIds(ids);

        // 再删除setmeal_dish表里的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
