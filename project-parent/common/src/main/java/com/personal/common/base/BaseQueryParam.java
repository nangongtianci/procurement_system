package com.personal.common.base;

import com.personal.common.base.marking.POJOSerializable;
import com.personal.common.base.page.BasePageQueryParam;
import com.personal.common.utils.validate.type.Update;

import javax.validation.constraints.NotBlank;

/**
 * 多条件查询基类
 * @author ylw
 * @date 18-5-18 下午3:18
 * @param
 * @return
 */
public class BaseQueryParam extends BasePageQueryParam implements POJOSerializable{
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "主键不能为空!",groups = {Update.class})
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
