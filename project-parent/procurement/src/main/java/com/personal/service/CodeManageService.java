package com.personal.service;

import com.msb.common.utils.result.Result;
import com.personal.entity.CodeManage;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 代码管理表 服务类
 * </p>
 *
 * @author ylw
 * @since 2018-10-28
 */
public interface CodeManageService extends IService<CodeManage> {

    /**
     * 更新代码管理级联更新商品
     * @param codeManage
     * @return
     */
    Result updateCasCadeGoods(CodeManage codeManage);
}
