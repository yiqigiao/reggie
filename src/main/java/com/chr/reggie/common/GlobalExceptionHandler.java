package com.chr.reggie.common;

import com.chr.reggie.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());
        String msg = e.getMessage();
        if (msg.contains("Duplicate entry")) {
            String[] s = msg.split(" ");
            s[2]  = s[2].substring(1, s[2].length() - 1);
            return R.error(s[2] + " 已存在！");
        }
        return R.error("异常");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> customException(CustomException e) {
        String msg = e.getMessage();
        log.info(msg);
        return R.error(msg);
    }
}
