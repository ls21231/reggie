package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.entity.Dish;
import com.ls.reggie.mapper.DishMapper;
import com.ls.reggie.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
