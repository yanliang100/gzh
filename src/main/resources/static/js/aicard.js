function showLink() {
    $(".mii-close").hide();
    $(".mii-open").show();
}

function hideLink(){
    $(".mii-open").hide();
    $(".mii-close").show();
}

//动态点赞
$(function(){
    $("#praise").click(function(){
        var praise_img = $("#praise-img");
        var text_box = $("#add-num");
        var praise_txt = $("#praise-txt em");
        var num=parseInt(praise_txt.text());
        if(praise_img.attr("src") == ("images/yizan.png")){
            $(this).html("<img src='images/zan.png' id='praise-img' class='animation' />");
            praise_txt.parent().removeClass("hover");
            text_box.show().html("<em class='add-animation'>-1</em>");
            $(".add-animation").removeClass("hover");
            num -=1;
            praise_txt.text(num)
        }else{
            $(this).html("<img src='images/yizan.png' id='praise-img' class='animation' />");
            praise_txt.parent().addClass("hover");
            text_box.show().html("<em class='add-animation'>+1</em>");
            $(".add-animation").addClass("hover");
            num +=1;
            praise_txt.text(num);
        }

        var isPraise=$("#isPraise").val();
        if(!isPraise!="1"){
            $.get("praise");
            $("#isPraise").val("1");
        }
    });
});

function shareLink(){
    /*wx.addPhoneContact({
        firstName:'钢丝球',
        mobilePhoneNumber:'17600100069',
        weChatNumber:'jmg20080808',
        success:function(res) {

        }
    });*/


// pages/newfunction/newfunction.js
    /*Page({

        /!**
         * 页面的初始数据
         *!/
        data: {
            phonenum: "1881881989"
        },
        //打电话  还是   添加到联系人
        viewClick01: function() {
            var that = this
            //显示“呼叫”、“添加联系人”弹窗
            wx.showActionSheet({
                itemList: ['呼叫', '添加联系人'],
                success: function(res) {
                    console.log("点击电话 res：", res)
                    if (res.tapIndex == 0) {//直接呼叫
                        wx.makePhoneCall({
                            phoneNumber: that.data.phonenum,
                            success: function(res_makephone) {
                                console.log("呼叫电话返回：", res_makephone)
                            }
                        })
                    } else if (res.tapIndex == 1) {//添加联系人
                        wx.addPhoneContact({
                            firstName: '关关',
                            mobilePhoneNumber: that.data.phonenum,
                            success: function(res_addphone) {
                                console.log("电话添加联系人返回：", res_addphone)
                            }
                        })
                    }
                }
            })
        },
        /!**
         * 生命周期函数--监听页面加载
         *!/
        onLoad: function(options) {

        },

        /!**
         * 生命周期函数--监听页面初次渲染完成
         *!/
        onReady: function() {

        },

        /!**
         * 生命周期函数--监听页面显示
         *!/
        onShow: function() {

        },

        /!**
         * 生命周期函数--监听页面隐藏
         *!/
        onHide: function() {

        },

        /!**
         * 生命周期函数--监听页面卸载
         *!/
        onUnload: function() {

        },

        /!**
         * 页面相关事件处理函数--监听用户下拉动作
         *!/
        onPullDownRefresh: function() {

        },

        /!**
         * 页面上拉触底事件的处理函数
         *!/
        onReachBottom: function() {

        },

        /!**
         * 用户点击右上角分享
         *!/
        onShareAppMessage: function() {

        }
    })*/
}