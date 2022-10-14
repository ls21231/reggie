package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.entity.Employee;
import com.ls.reggie.mapper.EmployeeMapper;
import com.ls.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @ls
 * @create 2022 -- 10 -- 12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
