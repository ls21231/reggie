package com.ls.reggie.controller;

import com.ls.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @ls
 * @create 2022 -- 10 -- 14
 */

@RestController
@RequestMapping("common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    /**
     * 文件上传功能
     * @param file  必须和前端的name保持一致
     *              这个file是一个临时文件，在请求结束后就被删除了
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        // 随机生成一个文件名
        UUID uuid = UUID.randomUUID();

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取文件的后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 给文件取一个新的名字
        String fileName = uuid.toString() + suffix;

        // 创建一个目录对象
        File dir = new File(basePath);
        // 判断目录是否存在
        if(!dir.exists()){
            // 创建该目录
            dir.mkdir();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    /**
     * 文件下载功能
     * @param name
     * @param response
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response){

        try {
            // 输入流，读取文件的内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流，将文件中的内容回显到浏览器
            ServletOutputStream outputStream = response.getOutputStream();


            // 相应给浏览器的，相应回的数据是什么类型的，是固定的
            response.setContentType("image/jpeg");
            // 输入流将文件的内容放到byte数组中，再将其通过输出流返回给浏览器
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            // 关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
