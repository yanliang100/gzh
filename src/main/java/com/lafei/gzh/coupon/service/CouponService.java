package com.lafei.gzh.coupon.service;

import com.lafei.gzh.coupon.bean.Coupon;
import com.lafei.gzh.coupon.bean.UserCoupon;
import com.lafei.gzh.coupon.mapper.CouponMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("couponService")
public class CouponService {
    @Resource
    private CouponMapper couponMapper;
    public Coupon queryTheLastCoupon() throws Exception {
        return couponMapper.selectTheLastCoupon();
    }

    public List<Map<String,Object>> queryCoupons(Integer goodsType,String weixinOpenid) throws Exception {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("goodsType",goodsType);
        params.put("enabled","0");
        params.put("weixinOpenid",weixinOpenid);
        return couponMapper.selectCoupons(params);
    }

    @Transactional
    public boolean recieveCoupon(String couponId,String weixinOpenid) throws Exception {
        boolean flg=false;
        Coupon coupon=couponMapper.selectById(Integer.parseInt(couponId));
        Integer publishAmount=coupon.getPublishAmount();
        Integer receiveAmount=coupon.getReceiveAmount();
        if(publishAmount>receiveAmount){
            UserCoupon userCoupon=new UserCoupon();
            userCoupon.setCouponId(coupon.getId());
            userCoupon.setCouponNumber(coupon.getCode());
            userCoupon.setState("0");
            userCoupon.setWeixinOpenid(weixinOpenid);
            if((flg=(couponMapper.insertUserCoupon(userCoupon)==1))){
                coupon.setReceiveAmount(++receiveAmount);
                flg=couponMapper.updateCoupon(coupon)==1;
            }
        }
        return flg;
    }

    public List<Coupon> queryAvailableCoupons(String weixinOpenid, String amount) throws Exception {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("weixinOpenid",weixinOpenid);
        params.put("amount",amount);
        return couponMapper.selectAvailableCoupons(params);
    }

    @Transactional
    public int updateUserCoupon(UserCoupon userCoupon) throws Exception {
        return couponMapper.updateUserCoupon(userCoupon);
    }

    public int queryCouponSize(Integer goodsType, String weixinOpenid) throws Exception {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("goodsType",goodsType);
        params.put("weixinOpenid",weixinOpenid);
        return couponMapper.selectCouponSize(params);
    }
}
