package com.lafei.gzh.ad.mapper;

import com.lafei.gzh.ad.bean.Ad;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdMapper {
    Ad selectAd(Short adPositionId) throws Exception;
}
