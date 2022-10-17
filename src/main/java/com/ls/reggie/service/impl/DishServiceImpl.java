package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.dto.DishDto;
import com.ls.reggie.entity.Dish;
import com.ls.reggie.entity.DishFlavor;
import com.ls.reggie.mapper.DishMapper;
import com.ls.reggie.service.DishFlavorService;
import com.ls.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，将数据保存到的菜品表和口味表中
     * @param dishDto
     */
    @Override
    @Transactional //涉及到两张表的插入操作，进行事务管理
    public void saveWithFlavor(DishDto dishDto) {

        // 将基本数据保存到数据库中
        this.save(dishDto);

        //将flavors数据保存到dish_flavor表中
        // 1. 先将flavors的dishId给赋上值，使其能够对应相应的菜品
        Long dishId = dishDto.getId();// 菜品的id

        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor: flavors) {
            flavor.setDishId(dishId);
        }
        // 2. 再调用flavorService 将flavors集合保存到数据库
       dishFlavorService.saveBatch(flavors);
    }


    /**
     * 回显修改菜品时的数据
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlavor(Long id) {

        log.info(id.toString());
        // 将菜品的基本信息查询出来
        Dish dish = this.getById(id);
        log.info(dish.getId().toString());
        // 返回的dto对象
        DishDto dishDto = new DishDto();
        // 拷贝
        BeanUtils.copyProperties(dish,dishDto);

        // 根据菜品id将菜品的口味信息查询出来
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        // 将口味信息设置到dto对象中
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 先将dish里的基本信息给更新了
        this.updateById(dishDto);
        
        // 再把菜品原来的口味给删除掉
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        
        // 添加当前提交的口味数据----dish_flavor的insert操作
        // 给所有的口味数据的dishId 赋值，dishId就是dishDto的id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }


}
