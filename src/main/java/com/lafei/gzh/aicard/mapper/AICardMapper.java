package com.lafei.gzh.aicard.mapper;

import com.lafei.gzh.aicard.bean.AICard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AICardMapper {
    int insert(AICard aiCard) throws Exception;
    List<AICard> selectAIUsers(Map<String,Integer> map) throws Exception;
    int selectPraiseCount() throws Exception;
    int selectAIUserCount() throws Exception;
    int updateByAICard(AICard aiCard) throws Exception;
    List<AICard> selectByWeixinOpenid(String weixinOpenid) throws Exception;
}
