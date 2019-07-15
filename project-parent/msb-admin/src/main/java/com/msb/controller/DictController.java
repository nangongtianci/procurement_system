package com.msb.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.msb.entity.Dict;
import com.msb.query.DictQueryParam;
import com.msb.service.DictService;
import com.msb.common.annotation.InsertMethodFlag;
import com.msb.common.annotation.PageQueryMethodFlag;
import com.msb.common.annotation.UpdateMethodFlag;
import com.msb.common.base.controller.BaseMsbAdminController;
import com.msb.common.enume.ModuleEnum;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.msb.common.utils.result.CommonResultMsg.assignModuleNameForName;
import static com.msb.common.utils.result.CommonResultMsg.assignModuleNameForPK;
import static com.msb.common.utils.result.RegUtils.matchesIds;

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
    @PageQueryMethodFlag
    @PostMapping("/page")
    public Result getPageByMultiParam(@RequestBody DictQueryParam param){
        EntityWrapper<Dict> ew = new EntityWrapper<>();

        if(StringUtils.isBlank(param.getPid())){
            ew.where("pid=''");
        }else{
            ew.where("pid={0}",param.getPid());
        }

        ew.orderBy("code",true);

        Page<Dict> page = new Page<>(param.getPageNow(),param.getPageSize());
        dictService.selectPage(page,ew);
        return render(page);
    }

    @PostMapping("/{id}")
    public Result getById(@PathVariable String id){
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.dict));
        }
        return render(dictService.selectById(id));
    }

    @InsertMethodFlag
    @PostMapping("add")
    public Result add(@RequestBody Dict dict){
        if(StringUtils.isBlank(dict.getName())){
            return Result.FAIL(assignModuleNameForName(ModuleEnum.dict));
        }

        if(StringUtils.isBlank(dict.getCode())){
            return Result.FAIL("编码不能为空！");
        }

        int ct = dictService.selectCount(new EntityWrapper<Dict>().where("code={0} or name={1}",dict.getCode(),dict.getName()));
        if(ct>0){
            return Result.FAIL("编码或名称已存在！");
        }

        return render(dictService.insert(dict));
    }

    @UpdateMethodFlag
    @PostMapping("update")
    public Result update(@RequestBody Dict dict){
        if(!matchesIds(dict.getId())){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.dict));
        }

        int ct = dictService.selectCount(new EntityWrapper<Dict>().
                where("(code={0} or name={1}) and id != {2}",dict.getCode(),dict.getName(),dict.getId()));
        if(ct>0){
            return Result.FAIL("编码或名称已存在！");
        }
        return render(dictService.updateById(dict));
    }

    @PostMapping("delete/{id}")
    public Result delete(@PathVariable String id) {
        if(!matchesIds(id)){
            return Result.FAIL(assignModuleNameForPK(ModuleEnum.dict));
        }

        Dict exists = dictService.selectOne(new EntityWrapper<Dict>().where("id={0}",id));
        if(StringUtils.isBlank(exists.getPid())){
            int ct = dictService.selectCount(new EntityWrapper<Dict>().where("pid={0}",id));
            if(ct > 0){
                return Result.FAIL("删除失败，请先删除子元素!");
            }
        }
        return render(dictService.deleteById(id));
    }


    public static void main(String[] args) {
        Dict dict = new Dict();
        dict.setId(UUIDUtils.getUUID());
        dict.setCode("unitOfCatty");
        dict.setName("元/斤");
        dict.setDesc("元/斤");
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        dict.setCreateUser("test");

        System.out.println(JSONObject.toJSONString(dict));
    }
}

