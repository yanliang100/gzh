package com.lafei.user.mapper;

import com.lafei.user.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAll() throws Exception;
    int insert( User weixinUser) throws Exception;
    int update( User weixinUser) throws Exception;
    User select(Integer id) throws Exception;
    User selectByWeixinOpenid(String weixinOpenid) throws Exception;
    int modifyPasswd(User user) throws Exception;
    User selectByMobileAndPasswd(User user) throws Exception;
    List<User> selectCashiers() throws Exception;
}
