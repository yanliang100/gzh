package com.lafei.gzh.goods.mapper;

import com.lafei.gzh.goods.bean.Channel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelMapper {
    List<Channel> selectKinds() throws Exception;
}
