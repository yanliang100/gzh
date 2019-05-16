package com.lafei.gzh.goods.mapper;

import com.lafei.gzh.goods.bean.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface GoodsMapper {
    Map<String, Object> selectByBarCode(@Param("goods_sn") String goods_sn) throws Exception;
    Goods selectBriefGoodByCode(@Param("goods_sn") String goods_sn) throws Exception;
}
