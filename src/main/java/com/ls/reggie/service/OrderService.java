package com.ls.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.reggie.entity.Orders;

/**
 * @ls
 * @create 2022 -- 10 -- 17
 */

public interface OrderService extends IService<Orders> {

    void submit(Orders orders);
}
