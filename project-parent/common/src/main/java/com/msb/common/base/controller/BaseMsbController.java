package com.msb.common.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.msb.common.cache.RedisService;
import com.msb.common.enume.UserTypeEnum;
import com.msb.common.utils.collections.ArrayUtils;
import com.msb.common.utils.base.StringUtils;
import com.msb.common.utils.collections.ListUtils;
import com.msb.common.utils.result.PaginationUtils;
import com.msb.common.utils.result.Result;
import com.msb.common.utils.token.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class BaseMsbController extends BaseController{
    @Autowired
    private RedisService redisService;

    protected  static Result render(Object obj,String ... msg){
        Result result;
        if(obj != null && obj instanceof Page){
            result = PaginationUtils.getResultObj((Page)obj);
        }else{
            result = Result.OK().setData(obj);
        }

        if (!ArrayUtils.isEmpty(msg)){
            result.setStatus(Result.FAIL_CODE);
            result.setMessage(StringUtils.join(msg,","));
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

    protected String getCid(String token){
        return TokenUtils.getUid(UserTypeEnum.customer,token,redisService);
    }

    /**
     * 获取字典中文名
     * @param param
     * @param code
     * @return
     */
    protected String getDictName(List<Map<String,Object>> param,String code){
        if(ListUtils.isEmpty(param) || StringUtils.isBlank(code)){
            return null;
        }

        for(Map<String,Object> tmp : param){
            if(code.equalsIgnoreCase(tmp.get("code").toString())){
                return tmp.get("name").toString();
            }
        }
        return null;
    }
}
