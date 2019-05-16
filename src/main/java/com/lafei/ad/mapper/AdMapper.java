package com.lafei.ad.mapper;

import com.lafei.ad.bean.Ad;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdMapper {
    Ad selectAd(Short adPositionId) throws Exception;
}
