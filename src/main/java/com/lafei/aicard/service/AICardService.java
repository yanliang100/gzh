package com.lafei.aicard.service;

import com.lafei.aicard.bean.AICard;
import com.lafei.aicard.mapper.AICardMapper;
import com.lafei.user.bean.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("aiCardService")
public class AICardService {
    @Resource
    public AICardMapper aiCardMapper;

    public int joinAICard(User weixinUser) throws Exception {
        AICard aiCard=new AICard(weixinUser.getWeixinOpenid(),weixinUser.getHeadimgurl(),"1","0");
        return aiCardMapper.insert(aiCard);
    }


    public List<AICard> findAIUsers(int page, int rows) throws Exception {
        Map<String,Integer> map=new HashMap<String,Integer>();
        map.put("page",page);
        map.put("rows",rows);
        return aiCardMapper.selectAIUsers(map);
    }

    public int findAIUserCount() throws Exception {
        return aiCardMapper.selectAIUserCount();
    }

    public int praiseAICard(User currentUser) throws Exception {
        AICard aiCard=new AICard();
        aiCard.setWeixinOpenid(currentUser.getWeixinOpenid());
        aiCard.setIsPraise("1");
        return aiCardMapper.updateByAICard(aiCard);
    }

    public boolean isJoinedAICard(User currentUser) throws Exception {
        List<AICard> aiCards=aiCardMapper.selectByWeixinOpenid(currentUser.getWeixinOpenid());
        return aiCards!=null && aiCards.size()==1;
    }

    public int findPraiseCount() throws Exception {
        return aiCardMapper.selectPraiseCount();
    }

    public AICard findMyCard(User currentUser) throws Exception {
        List<AICard> aiCards=aiCardMapper.selectByWeixinOpenid(currentUser.getWeixinOpenid());
        return aiCards!=null && aiCards.size()>0?aiCards.get(0):null;
    }
}
