package com.personal.common.base.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.personal.common.utils.base.StringUtils;
import com.personal.common.utils.result.PaginationUtils;
import com.personal.common.utils.result.Result;

import java.util.Objects;

public class BaseMsbAdminController extends BaseController{

    protected Result render(Object obj,String ... msg){
//        if(Objects.isNull(obj)
//                || (obj instanceof Page && ((Page)obj).getTotal()<=0)
//                || (obj instanceof Collection && ((Collection) obj).isEmpty())
//                || (obj instanceof Map && ((Map) obj).isEmpty())
//                || (obj.getClass().isArray() && ((Object[])obj).length <= 0)){
//            return Result.EMPTY();
//        }

        Result result;
        if(obj != null && obj instanceof Page){
            result = PaginationUtils.getResultObj((Page)obj);
        }else{
            result = Result.OK(obj);
        }

        if (Objects.isNull(msg)){
            result.setMessage("msg:"+StringUtils.join(msg,","));
        }
        return result;
    }


    protected Result render(boolean rt){
        return rt?Result.OK():Result.FAIL();
    }

    public static void main(String[] args) {
        int a[] = new int[]{1,2,3};
        int[] b = new int[10];

    }
}
