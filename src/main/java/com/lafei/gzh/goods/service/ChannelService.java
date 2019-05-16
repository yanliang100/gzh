package com.lafei.gzh.goods.service;

import com.lafei.gzh.goods.bean.Channel;
import com.lafei.gzh.goods.mapper.ChannelMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service("channelService")
public class ChannelService {
    @Resource
    private ChannelMapper channelMapper;

    public List<Channel> queryKinds() throws Exception {
        return channelMapper.selectKinds();
    }
}
