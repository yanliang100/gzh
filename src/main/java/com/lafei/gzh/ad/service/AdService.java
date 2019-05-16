package com.lafei.gzh.ad.service;

import com.lafei.gzh.ad.bean.Ad;
import com.lafei.gzh.ad.mapper.AdMapper;
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
