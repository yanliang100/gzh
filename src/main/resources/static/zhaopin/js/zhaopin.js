function shareLink(){
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
                timestamp:result.timestamp,
                // 必填，生成签名的随机串
                nonceStr:result.nonceStr,
                // 必填，签名，见附录1
                signature:result.signature,
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                jsApiList : [
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage',
                    'onMenuShareQQ',
                    'onMenuShareWeibo',
                    'onMenuShareQZone'
                    ]
            });
            wx.error(function(res) {
                alert("出错了：" + res.errMsg);//这个地方的好处就是wx.config配置错误，会弹出窗口哪里错误，然后根据微信文档查询即可。
            });
            wx.ready(function() {
                wx.onMenuShareTimeline({
                    title: '招聘', // 分享标题
                    link: 'zhaopin', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl:'zhaopin/wzp_logo.png', // 分享图标
                    success: function () {
                        alert('分享成功');
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        alert('分享失败');
                        // 用户取消分享后执行的回调函数
                    }
                });
                wx.onMenuShareAppMessage({
                    title: '招聘', // 分享标题
                    desc: '东方有泉家居馆最新招聘信息', // 分享描述
                    link: 'zhaopin', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl: 'zhaopin/wzp_logo.png', // 分享图标
                    type: 'link', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                        alert('分享成功');
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        alert('分享失败');
                        // 用户取消分享后执行的回调函数
                    }
                });
            });
        }
    });
}

function dianzan(e){
    var zan=new Number($("#zan").html());
    zan++;
    $("#zan").html(zan);
    $(e).removeAttr("onclick");
    alert("点赞成功");
}