package com.msb.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.msb.entity.SourceImage;
import com.msb.entity.SourceImageGroup;
import com.msb.query.ResourceImageGroupQueryParam;
import com.msb.query.ResourceImageQueryParam;
import com.msb.service.SourceImageGroupService;
import com.msb.service.SourceImageService;
import com.personal.common.annotation.InsertMethodFlag;
import com.personal.common.annotation.PageQueryMethodFlag;
import com.personal.common.annotation.UpdateMethodFlag;
import com.personal.common.base.controller.BaseMsbAdminController;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统资源图片表 前端控制器
 * </p>
 *
 * @author ylw
 * @since 2019-04-25
 */
@RestController
@RequestMapping("/source/image")
public class SourceImageController extends BaseMsbAdminController{
    @Autowired
    private SourceImageService sourceImageService;
    @Autowired
    private SourceImageGroupService sourceImageGroupService;

    /**
     * 分组多参数分页查询
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("/group/page")
    public Result getGroupPageByMultiParam(@RequestBody ResourceImageGroupQueryParam param){
        EntityWrapper<SourceImageGroup> ew = new EntityWrapper<>();

        if(StringUtils.isNotBlank(param.getId())){
            ew.where("id={0}",param.getId());
        }

        if(StringUtils.isNotBlank(param.getName())){
            ew.and("name={0}",param.getName());
        }

        Page<SourceImageGroup> page = new Page<>(param.getPageNow(),param.getPageSize());
        sourceImageGroupService.selectPage(page,ew);
        return render(page);
    }

    /**
     * 多参数分页查询
     * @param param
     * @return
     */
    @PageQueryMethodFlag
    @PostMapping("page")
    public Result getPageByMultiParam(@RequestBody ResourceImageQueryParam param){
        EntityWrapper<SourceImage> ew = new EntityWrapper<>();

        if(StringUtils.isNotBlank(param.getId())){
            ew.where("id={0}",param.getId());
        }

        if(StringUtils.isNotBlank(param.getGid())){
            ew.and("gid={0}",param.getGid());
        }

        if(StringUtils.isNotBlank(param.getName())){
            ew.and("name={0}",param.getName());
        }

        Page<SourceImage> page = new Page<>(param.getPageNow(),param.getPageSize());
        sourceImageService.selectPage(page,ew);
        return render(page);
    }

    @InsertMethodFlag
    @PostMapping("group/add")
    public Result addGroup(@RequestBody SourceImageGroup sourceImageGroup){
        return render(sourceImageGroupService.insert(sourceImageGroup));
    }

    @InsertMethodFlag
    @PostMapping("add")
    public Result add(@RequestBody SourceImage sourceImage){
        return render(sourceImageService.insert(sourceImage));
    }

    @UpdateMethodFlag
    @PostMapping("group/update")
    public Result updateGroup(@RequestBody SourceImageGroup sourceImageGroup){
        if(StringUtils.isBlank(sourceImageGroup.getId())){
            return render("主键不能为空！");
        }
        return render(sourceImageGroupService.updateById(sourceImageGroup));
    }

    @UpdateMethodFlag
    @PostMapping("update")
    public Result update(@RequestBody SourceImage sourceImage){
        return render(sourceImageService.updateById(sourceImage));
    }


    @PostMapping("group/batch/delete")
    public Result batchInDeleteGroup(@RequestBody String param){
        String ids = (String) getParamByKey(param,"ids");
        if(null == ids){
            return render("主键不能为空!");
        }

        EntityWrapper<SourceImage> ew = new EntityWrapper<>();
        ew.in("id",ids.split(","));
        int ct = sourceImageService.selectCount(ew);
        if(ct>0){
            return render("先移除子元素，才能正常移除组信息!");
        }

        return render(sourceImageGroupService.deleteBatchIds(Lists.newArrayList(ids.split(","))));
    }

    @PostMapping("batch/delete")
    public Result batchInDelete(@RequestBody String param){
        String ids = (String) getParamByKey(param,"ids");
        if(null == ids){
            return render("主键不能为空!");
        }
        return render(sourceImageService.deleteBatchIds(Lists.newArrayList(ids.split(","))));
    }

    @PostMapping("group/{id}")
    public Result getGroupById(@PathVariable String id){
        return render(sourceImageGroupService.selectById(id));
    }

    @PostMapping("{id}")
    public Result getById(@PathVariable String id){
        return render(sourceImageGroupService.selectById(id));
    }
}

