package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.common.CustomerException;
import com.ls.reggie.entity.Category;
import com.ls.reggie.entity.Dish;
import com.ls.reggie.entity.Setmeal;
import com.ls.reggie.mapper.CategoryMapper;
import com.ls.reggie.service.CategoryService;
import com.ls.reggie.service.DishService;
import com.ls.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id来删除分类,删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联菜品，如果已经关联，抛出一个异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 != 0){
            // 抛出一个异常
            throw new CustomerException("该菜品分类已经包含了菜品，不能删除");
        }
        // 查询当前分类是否关联套餐，如果已经关联，抛出一个异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 != 0){
            // 抛出一个异常
            throw new CustomerException("该菜品分类已经包含了套餐，不可删除");
        }
        // 正常删除分类
        super.removeById(id);
    }
}
