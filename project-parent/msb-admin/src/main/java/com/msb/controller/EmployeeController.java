package com.msb.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 员工表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    public static void main(String[] args) {
        int a = 10,b=10;
        modify(a,b);
        System.out.println(a);
        System.out.println(b);
        // 有参数无返回值
        // 无参无返回值
        // 无参有返回值

    }

    private static void modify(int a,int b){
        a = 0;
        b = 0;
    }
}

