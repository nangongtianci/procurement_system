package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.base.controller.BaseMsbController;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.collections.ArrayUtils;
import com.msb.common.utils.collections.Collections3;
import com.msb.common.utils.collections.ListUtils;
import com.msb.common.utils.result.Result;
import com.msb.entity.Dict;
import com.msb.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.msb.common.utils.result.CommonResultMsg.assignFieldNotNull;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-05-27
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseMsbController {
//    public static void main(String[] args) {
//        for(int i=0;i<5;i++){
//            System.out.println(UUIDUtils.getUUID());
//        }
//
//        //System.out.println(MD5Util.getStringMD5("21232f297a57a5a743894a0e4a801fc3"));
//        //.out.println(MD5Util.getStringMD5("21232f297a57a5a743894a0e4a801fc3"+"b90ebb3e2caf4222ab8c231df1839aec"));
//    }

    @Autowired
    private DictService dictService;

    /**
     * 查询分类元素
     * @param pid
     * @return
     */
    @PostMapping("/elements/{pid}")
    public Result getElementsByPid(@PathVariable String pid){
        EntityWrapper<Dict> ew = new EntityWrapper<>();
        ew.setSqlSelect("id,name,code");
        ew.where("pid={0}",pid);
        return render(dictService.selectMaps(ew));
    }

    /**
     * 查询指定分类
     * @param param
     * @return
     */
    @PostMapping("/assign/type")
    public Result getAssignType(@RequestBody String param){
        Object ids = getParamByKey(param,"ids");
        if(Objects.isNull(ids) || StringUtils.isBlank(ids.toString())){
            return render(null,assignFieldNotNull("分类主键"));
        }
        EntityWrapper<Dict> ew = new EntityWrapper<>();
        ew.setSqlSelect("id,name,code");
        ew.in("id",ids.toString().split(","));
        List<Map<String, Object>> plist = dictService.selectMaps(ew);
        if(!ListUtils.isEmpty(plist)){
            for(Map<String, Object> tmp : plist){
                EntityWrapper<Dict> sew = new EntityWrapper<>();
                sew.setSqlSelect("id,name,code");
                sew.where("pid={0}",tmp.get("id").toString());
                tmp.put("subs",dictService.selectMaps(sew));
            }
        }
        return render(plist);
    }
}

