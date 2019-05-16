package com.lafei.gzh.goods.service;

import com.lafei.gzh.goods.bean.Goods;
import com.lafei.gzh.goods.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;

@Service("goodsService")
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    public Map<String, Object> queryByBarCode(String barCode) throws Exception {
        return goodsMapper.selectByBarCode(barCode);
    }

    public Goods queryBriefGoodByCode(String barCode) throws Exception {
        return goodsMapper.selectBriefGoodByCode(barCode);
    }
}
