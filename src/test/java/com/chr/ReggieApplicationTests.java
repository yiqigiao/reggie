package com.chr;

import com.chr.reggie.pojo.DishFlavor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ReggieApplicationTests {

    @Test
    void contextLoads() {
        String s = "'chr'";
        String substring = s.substring(1,s.length()-1);
        System.out.println(substring);
    }
    @Test
    void listToString() {
        String s = "1413384757047271425,1413385247889891330";
        String[] split = s.split(",");
        for (String s1 : split) {
            System.out.println(s1);
        }

    }
}
