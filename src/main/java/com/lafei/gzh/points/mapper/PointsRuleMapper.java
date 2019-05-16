package com.lafei.gzh.points.mapper;

import com.lafei.gzh.points.bean.PointsRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointsRuleMapper {
    PointsRule selectPointsRule(Integer actualIntPrice) throws Exception;
}
