package com.lafei.ad.service;

import com.lafei.ad.bean.Ad;
import com.lafei.ad.mapper.AdMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("adService")
public class AdService {
    @Resource
    public AdMapper adMapper;
    public Ad queryXptj() throws Exception {
        return adMapper.selectAd((short) 2);
    }
    public Ad querySpyh() throws Exception {
        return adMapper.selectAd((short) 3);
    }

    public Ad queryYhhd()  throws Exception {
        return adMapper.selectAd((short) 4);
    }
}
