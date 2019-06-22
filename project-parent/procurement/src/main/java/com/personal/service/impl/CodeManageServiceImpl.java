package com.personal.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.msb.common.utils.result.Result;
import com.personal.entity.CodeManage;
import com.personal.entity.Goods;
import com.personal.mapper.CodeManageMapper;
import com.personal.mapper.GoodsMapper;
import com.personal.service.CodeManageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 代码管理表 服务实现类
 * </p>
 *
 * @author ylw
 * @since 2018-10-28
 */
@Transactional
@Service
public class CodeManageServiceImpl extends ServiceImpl<CodeManageMapper, CodeManage> implements CodeManageService {

    @Autowired
    private CodeManageMapper codeManageMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Result updateCasCadeGoods(CodeManage codeManage) {
        codeManageMapper.updateById(codeManage);
        Goods goods = new Goods();
        goods.setCodeImgPath(codeManage.getProductImgPath());
        EntityWrapper<Goods> ew = new EntityWrapper();
        ew.where("cmid={0}",codeManage.getId());
        goodsMapper.update(goods,ew);
        return Result.OK();
    }
}
