package com.ls.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ls.reggie.common.R;
import com.ls.reggie.entity.User;
import com.ls.reggie.service.UserService;
import com.ls.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ls
 * @create 2022 -- 10 -- 16
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送验证吗，模拟
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        log.info(user.getPhone());

        // 获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //
            log.info("code = " + code);

//            // 将验证码存储到Session
//            session.setAttribute(phone,code);

            // 将验证码缓存到redis中
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session) {
        log.info(map.toString());

        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();

//        // 从session中获取保存的验证码
//        String codeInSession = session.getAttribute(phone).toString();

        // 从redis缓存中获取验证码
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

        // 进行验证码的比对
        if(codeInSession != null && codeInSession.equals(code)){
            // 如果比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            // 判断当前的手机号对应的用户是否是新用户
            if(user == null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            // 此时已经登录成功，将缓存中的验证码删除掉
            redisTemplate.delete(phone);
            return R.success(user);
        }

        return R.error("登陆失败");
    }
}
