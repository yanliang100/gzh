function trans(){
    $.ajax({
        type:"post",
        url:"jsapisign",//自己填写请求地址
        data:{
            url: location.href.split('#')[0]
        },
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        async: true,
        success:function(result){
            wx.config({
                // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                debug: false,
                // 必填，公众号的唯一标识
                appId: result.appId,
                // 必填，生成签名的时间戳
                timestamp:""+result.timestamp,
                // 必填，生成签名的随机串
                nonceStr:result.nonceStr,
                // 必填，签名，见附录1
                signature:result.signature,
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                jsApiList: ['chooseImage', 'uploadImage'] // 必填，需要使用的JS接口列表，开发文档上有所有接口名称，根据需要选用就好。
            });
        }
    })

    wx.error(function(res) {
        alert("出错了：" + res.errMsg);//这个地方的好处就是wx.config配置错误，会弹出窗口哪里错误，然后根据微信文档查询即可。
    });

    wx.ready(function() {
        wx.chooseImage({
            count: 1, // 默认9
            sizeType: ['original', 'compressed'], // 指定是原图还是压缩图，默认都有
            sourceType: ['album', 'camera'], // 指定来源是相册还是相机，默认都有
            success: function (res) {
                var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                wx.uploadImage({
                    localId: localIds.toString(), // 需要上传的图片的ID，由chooseImage接口获得
                    isShowProgressTips: 1, // 进度提示
                    success: function (res) {
                        location.href ="bce_jpa?mediaId=" + res.serverId; // 返回图片的服务器端ID，即mediaId
                    },
                    fail: function (res) {
                        alertModal('图片上传失败，请重试');
                    }
                });
            }
        });
    });
}