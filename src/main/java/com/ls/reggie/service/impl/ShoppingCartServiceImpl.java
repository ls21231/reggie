package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.entity.ShoppingCart;
import com.ls.reggie.mapper.ShoppingCartMapper;
import com.ls.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @ls
 * @create 2022 -- 10 -- 17
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
