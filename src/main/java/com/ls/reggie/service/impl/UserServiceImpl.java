package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.entity.User;
import com.ls.reggie.mapper.UserMapper;
import com.ls.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ls
 * @create 2022 -- 10 -- 16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
