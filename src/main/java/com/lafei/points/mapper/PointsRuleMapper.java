package com.lafei.points.mapper;

import com.lafei.points.bean.PointsRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointsRuleMapper {
    PointsRule selectPointsRule(Integer actualIntPrice) throws Exception;
}
