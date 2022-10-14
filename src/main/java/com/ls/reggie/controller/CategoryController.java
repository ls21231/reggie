package com.ls.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.reggie.common.R;
import com.ls.reggie.entity.Category;
import com.ls.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类的控制类
 * @ls
 * @create 2022 -- 10 -- 14
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 添加菜品分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> category(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加菜品成功");
    }


    /**
     * 菜品分类页面展示请求
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize) {
        // 创建分页器
        Page<Category> pageInfo = new Page<>(page,pageSize);

        // 创建查询条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 排序
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 删除没有进行菜品绑定的菜品分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids) {

        // 根据条件进行删除
        categoryService.remove(ids);
        return R.success("分类删除操作成功");
    }


    /**
     * 修改分类的信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }
}
