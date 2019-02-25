package com.personal.common.utils.result;

import com.baomidou.mybatisplus.plugins.Page;
import com.personal.common.base.page.AbstractPageQueryParam;

/**
 * 分页工具
 * @author ylw
 * @date 18-7-15 上午10:59
 * @param
 * @return
 */
public class PaginationUtils {
    /**
     * @desc 获取分页对象
     * @param obj 列表对象（一般情况下）
     * @param total 符合条件的个数
     * @param pageNow 当前页
     * @param pageSize 每页多少条
     * @return
     */
    public static Result getResultObj(Object obj,int total,
                                      int pageNow,int pageSize){
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setPageNow(pageNow);
        pageResult.setPageSize(pageSize);
        pageResult.setRows(obj);
        return Result.OK(pageResult);
    }

    public static Result getResultObj(Object obj,int total,AbstractPageQueryParam param){
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setPageNow(param.getPageNow());
        pageResult.setPageSize(param.getPageSize());
        pageResult.setRows(obj);
        return Result.OK(pageResult);
    }

    public static <T> Result getResultObj(Page<T> page){
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setPageNow(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setRows(page.getRecords());
        return Result.OK(pageResult);
    }
}
