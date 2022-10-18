package com.ls.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ls.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ls
 * @create 2022 -- 10 -- 17
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
