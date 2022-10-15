package com.ls.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.reggie.dto.DishDto;
import com.ls.reggie.entity.Dish;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */
public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto getDishWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
