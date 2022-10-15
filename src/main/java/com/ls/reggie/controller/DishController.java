package com.ls.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.reggie.common.R;
import com.ls.reggie.dto.DishDto;
import com.ls.reggie.entity.Category;
import com.ls.reggie.entity.Dish;
import com.ls.reggie.service.CategoryService;
import com.ls.reggie.service.DishFlavorService;
import com.ls.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ls
 * @create 2022 -- 10 -- 15
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        // 涉及到两张表的插入，自定义方法
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品新增成功");
    }


    /**
     * 菜品的展示
     *     因为菜品表 dish中没有菜品的名字，所有需要到category表中去获取，逻辑稍显复杂
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R<Page> list(int page,int pageSize,String name) {

        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        // 1. 将 Dish表中查询的数据pageInfo 放到 dishDtoPage中，除了records（这是具体的数据） 进行拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        // 2. 将pageInfo中的records的categoryName 赋值后再拷贝到 dishDtoPage
        //     获取pageInfo里的records
        List<Dish> records = pageInfo.getRecords();
        //     进行赋值拷贝
        List<DishDto> list = records.stream().map((item) -> {
            // 接受categoryName 赋值后的DishDto 对象
            DishDto dishDto = new DishDto();
            // 将records里的每一项都拷贝到dishDto中
            BeanUtils.copyProperties(item,dishDto);
            // 在 item中得到所属菜品分类的id -> categoryId
            Long id = item.getCategoryId();// 菜品id
            // 再根据得到的菜品分类id到category表中去查询数据，得到相应的菜品分类对象
            Category category = categoryService.getById(id);
            if(category != null){
                // 菜品分类的Name 获取到了，再赋值给dishDto的categoryName属性
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }


    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id){
        DishDto dishWithFlavor = dishService.getDishWithFlavor(id);
        return R.success(dishWithFlavor);
    }

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        // 方法重写
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }
}
