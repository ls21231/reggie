package com.ls.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.reggie.entity.Category;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
