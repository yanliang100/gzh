package com.lafei.points.service;

import com.lafei.points.bean.PointsRule;
import com.lafei.points.mapper.PointsRuleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("pointsRuleService")
public class PointsRuleService {

    @Resource
    public PointsRuleMapper pointsRuleMapper;
    public Integer getRewardPoints(int actualIntPrice) throws Exception {
        PointsRule pointsRule=pointsRuleMapper.selectPointsRule(actualIntPrice);
        if(pointsRule!=null){
            return actualIntPrice*pointsRule.getTypePoints();
        }
        return actualIntPrice;
    }
}
