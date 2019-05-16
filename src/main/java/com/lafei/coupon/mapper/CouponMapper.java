package com.lafei.coupon.mapper;

import java.util.List;
import java.util.Map;

import com.lafei.coupon.bean.Coupon;
import com.lafei.coupon.bean.UserCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponMapper {
    Coupon selectTheLastCoupon() throws Exception;
    List<Map<String,Object>> selectCoupons(Map<String,Object> patrams) throws Exception;
    Coupon selectById(Integer id) throws Exception;
    int updateCoupon(Coupon coupon) throws Exception;
    int insertUserCoupon(UserCoupon userCoupon) throws Exception;
    int updateUserCoupon(UserCoupon userCoupon) throws Exception;
    List<Coupon> selectAvailableCoupons(Map<String,Object> params) throws Exception;
    int selectCouponSize(Map<String, Object> params) throws Exception;
}
