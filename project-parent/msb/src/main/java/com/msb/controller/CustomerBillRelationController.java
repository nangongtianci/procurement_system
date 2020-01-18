package com.msb.controller;


import com.msb.service.CustomerBillRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户账单关系表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-06-13
 */
@RestController
@RequestMapping("/customerBillRelation")
public class CustomerBillRelationController {
    @Autowired
    private CustomerBillRelationService customerBillRelationService;


}

