package com.ls.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ls.reggie.entity.AddressBook;
import com.ls.reggie.mapper.AddressBookMapper;
import com.ls.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @ls
 * @create 2022 -- 10 -- 17
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
