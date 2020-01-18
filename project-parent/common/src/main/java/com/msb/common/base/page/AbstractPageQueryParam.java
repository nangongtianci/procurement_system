package com.msb.common.base.page;

import com.msb.common.constant.SysConstant;
import io.swagger.annotations.ApiModelProperty;

/**
  * @Title: AbstractPageQueryParam
  * @Package: com.personal.common.base.page
  * @Description: 分页查询查询服务接口(由具体的查询接口继承)
  * @Author: ylw
  * @date: 2017/3/20
  * @Version: V1.0
  */
public abstract class AbstractPageQueryParam implements PageQueryParam {
    /**
     * @desc 每页多少条
     */
    @ApiModelProperty(value="每页多少条",name="pageSize")
    private int pageSize = SysConstant.PAGE_SIZE_DEFAULT;

    /**
     * @desc 当前第几页 (外部分页显示使用)
     */
    @ApiModelProperty(value="当前第几页 (外部分页显示使用)",name="pageNow")
    private int pageNow = SysConstant.PAGE_NOW_DEFAULT;

    /**
     * @desc 开始索引位置
     */
    @ApiModelProperty(hidden = true)
    private int offSet;

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.offSet = (pageNow-1)*pageSize;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }
}
