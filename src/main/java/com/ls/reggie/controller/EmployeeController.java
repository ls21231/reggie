package com.ls.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ls.reggie.common.R;
import com.ls.reggie.entity.Employee;
import com.ls.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @ls
 * @create 2022 -- 10 -- 12
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    // 登录功能
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {

        // 1. 将页面的提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据页面的传来的username进行数据库查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. 如果没有查询到则返回登录失败的结果
        if(emp == null){
            R.error("登录失败");
        }

        // 4. 密码比对，不成功则返回登录失败结果
        if(!password.equals(emp.getPassword())){
            R.error("登录失败");
        }

        // 5. 查看员工状态，如果用户被禁用，将状态进行返回
        if(emp.getStatus() == 0){
            R.error("账号被禁用");
        }

        // 6. 登录成功，将用户的id保存金session并进行返回
        httpServletRequest.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    // 退出功能
    @PostMapping ("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest){

        System.out.println(httpServletRequest.getSession().getAttribute("employee"));
        // 删除session中的用户信息，推出登录
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    // 新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        // 密码进行md5加密
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        // 设置密码
        employee.setPassword(password);


        /**
         * 通过MybatisPlus自动填充功能来实现
         */
//        // 设置创建时间
//        employee.setCreateTime(LocalDateTime.now());
//        // 设置更新时间
//        employee.setUpdateTime(LocalDateTime.now());
//        // 设置创建人的身份
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        // 设置更新人的身份
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));

        employeeService.save(employee);
        return R.success("新增成功");
    }



    /**
     * 展示页面的员工信息
     * @param page 页数
     * @param pageSize 页信息条数
     * @param name 搜索框的信息
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //构造分页查询条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //构造排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 处理编辑操作和禁用启用的请求
     * @param request
     * @param employee 修改的员工
     * @return
     */
    @PutMapping
    public R<String> upData(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());


        /**
         * 通过MybatisPlus自动填充功能来实现
         */
//        // 设置修改时间和修改人
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);
        return R.success("修改成功");
    }


    /**
     * 处理编辑操作时的回显信息请求
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        // 通过员工id查询
        Employee employee = employeeService.getById(id);
        if(employee != null)
            return R.success(employee);
        return R.error("没有查询到相关信息");
    }


}
