package com.ls.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @ls
 * @create 2022 -- 10 -- 13
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody//返回JSON数据
@Slf4j
public class GlobalException {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String msg = ex.getMessage();
        log.info(msg);
        if(msg.contains("Duplicate entry")){
            String[] split = msg.split(" ");
            String s = split[2];
            return R.error(s+"已存在");
        }
        return R.error("未知错误");
    }




    @ExceptionHandler(CustomerException.class)
    public R<String> exceptionHandler(CustomerException ex){

        return R.error(ex.getMessage());
    }
}
