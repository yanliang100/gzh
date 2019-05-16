package com.lafei.goods.service;

import com.lafei.goods.bean.Channel;
import com.lafei.goods.mapper.ChannelMapper;
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
