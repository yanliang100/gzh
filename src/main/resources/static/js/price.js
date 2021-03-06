function openSaoyiSao(){
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
                jsApiList : [ 'checkJsApi', 'scanQRCode' ]
            });
        }
    })

    wx.error(function(res) {
        alert("出错了：" + res.errMsg);//这个地方的好处就是wx.config配置错误，会弹出窗口哪里错误，然后根据微信文档查询即可。
    });

    wx.ready(function() {
        wx.checkJsApi({
            jsApiList : ['scanQRCode'],
            success : function(res) {

            }
        });
        wx.scanQRCode({
            needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType : ["barCode"], // 可以指定扫二维码还是一维码，默认二者都有
            success : function(res) {
                //扫码后获取结果参数赋值给Input
                var bar=res.resultStr;
                //商品条形码，取","后面的
                if (bar.indexOf(",") >= 0) {
                    var tempArray = bar.split(',');
                    var barCode = tempArray[1];
                    location.href ="ask_price?barCode=" + barCode;
                } else {
                    $.toast("请对准条形码扫码!");
                }
            }
        });
    });
}