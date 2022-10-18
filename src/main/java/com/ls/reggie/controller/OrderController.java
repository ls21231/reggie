package com.ls.reggie.controller;

import com.ls.reggie.common.R;
import com.ls.reggie.entity.Orders;
import com.ls.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ls 
 * @create 2022 -- 10 -- 17
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
//        Collection collection = new ArrayList();
//        List list = new ArrayList();
        return R.success("下单成功");
    }
}
