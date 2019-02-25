package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.msb.entity.Dict;
import com.msb.query.DictQueryParam;
import com.msb.service.DictService;
import com.personal.common.base.controller.BaseMsbAdminController;
import com.personal.common.enume.ModuleEnum;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.personal.common.utils.result.CommonResultMsg.assignModuleNameForName;
import static com.personal.common.utils.result.CommonResultMsg.assignModuleNameForPK;
import static com.personal.common.utils.result.RegUtils.matchesIds;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-01-16
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseMsbAdminController {

    @Autowired
    private DictService dictService;

    /**
     * 多参数分页查询
     * @param param
     * @return
     */
    @PostMapping("/page")
    public Result getPageByMultiParam(DictQueryParam param){
        EntityWrapper<Dict> ew = new EntityWrapper<>();

        if(StringUtils.isBlank(param.getPid())){
            ew.where("pid=''");
        }else{
            ew.where("pid={0}",param.getPid());
        }

        Page<Dict> page = new Page<>(param.getPageNow(),param.getPageSize());
        dictService.selectPage(page,ew);
        return render(page);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.dict));
        }
        return render(dictService.selectById(id));
    }


    @PostMapping("add")
    public Result add(Dict dict){
        if(StringUtils.isBlank(dict.getName())){
            return Result.FAIL(assignModuleNameForName(ModuleEnum.dict));
        }

        if(StringUtils.isBlank(dict.getCode())){
            return Result.FAIL("编码不能为空！");
        }
        return render(dictService.insert(dict));
    }


    @PostMapping("update")
    public Result update(Dict dict){
        if(!matchesIds(dict.getId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.dict));
        }
        return render(dictService.updateById(dict));
    }

    @PostMapping("delete/{id}")
    public Result delete(@PathVariable String id) {
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.dict));
        }
        return render(dictService.deleteById(id));
    }
}

