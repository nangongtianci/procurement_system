package com.personal.common.base.controller;

import com.alibaba.fastjson.JSONObject;
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
            result.setStatus(Result.FAIL_CODE);
            result.setMessage("msg:"+StringUtils.join(msg,","));
        }
        return result;
    }


    protected Result render(boolean rt){
        return rt?Result.OK():Result.FAIL();
    }

    /**
     * @desc 从json串格式的请求参数中，获取指定的字段值(本方法适合那些请求参数非常少的情况)
     * @param param json串
     * @param fieldName key名称
     * @return
     */
    protected Object getParamByKey(String param,String fieldName){
        if(StringUtils.isBlank(param) || !param.contains(fieldName)){
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(param);
        return jsonObject.getString(fieldName);
    }
}
