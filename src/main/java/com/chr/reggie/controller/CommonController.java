package com.chr.reggie.controller;

import com.chr.reggie.common.R;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${file.upload.url}")
    private String uploadUrl;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUId生成文件名，以免造成重复
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(uploadUrl);//如果不确定位置可以使用绝对路径进行测试
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(uploadUrl + fileName)); // 文件保存
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            // 输入流，通过输入流读取文件内容
            fileInputStream = new FileInputStream(new File(uploadUrl + name));

            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
