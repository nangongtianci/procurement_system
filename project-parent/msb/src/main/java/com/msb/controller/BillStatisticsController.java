package com.msb.controller;

import com.msb.common.base.controller.BaseMsbController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "账单统计controller",description = "账单统计控制器")
@RestController
@RequestMapping("/bill/stat")
public class BillStatisticsController extends BaseMsbController {

}
