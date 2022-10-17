package com.ls.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.reggie.dto.SetmealDto;
import com.ls.reggie.entity.Setmeal;

import java.util.List;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */
public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
