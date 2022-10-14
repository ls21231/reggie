package com.ls.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ls.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ls
 * @create 2022 -- 10 -- 12
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
