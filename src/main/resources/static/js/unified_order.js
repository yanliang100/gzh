$(function(){
    //出现浮动层
    $(".ljzf_but").click(function(){
        //$(".ftc_wzsf").show();
        pay();
    });
    //关闭浮动
    $(".close").click(function(){
        $(".ftc_wzsf").hide();
    });
    //数字显示隐藏
    $(".xiaq_tb").click(function(){
        $(".numb_box").slideUp(500);
    });
    $(".mm_box").click(function(){
        $(".numb_box").slideDown(500);
    });
    //----
    var i = 0;
    $(".nub_ggg li a").click(function(){
        i++
        if(i<6){
            $(".mm_box li").eq(i-1).addClass("mmdd");
        }else{
            $(".mm_box li").eq(i-1).addClass("mmdd");
            setTimeout(function(){
                location.href="cg?orderSn="+$("#orderSn").val()+"&amount="+$("#amount").val()+"&couponId="+$("#couponId").val();
            },500);
            //window.document.location="cg.html"
        }
    });
    $(".nub_ggg li .del").click(function(){
        if(i>0){
            i--
            $(".mm_box li").eq(i).removeClass("mmdd");
            i==0;
        }
        //alert(i);
    });
});

function pay() {
    var out_trade_no=$("[name='out_trade_no']").text().trim();
    var totalFee=$("[name='totalFee']").text().trim();
    $.getJSON("doUnifiedOrder",{"out_trade_no":out_trade_no,"totalFee":totalFee},function(result) {
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady',onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady',onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady',onBridgeReady);
            }
        } else {
            onBridgeReady(result);
        }
    });
}

function onBridgeReady(params){
    WeixinJSBridge.invoke( 'getBrandWCPayRequest',params,function(res){
            if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                console.log('支付成功');
                //1支付成功后跳转的页面
                //2发起公众号内通知
            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                console.log('支付取消');
            }else if(res.err_msg == "get_brand_wcpay_request:fail"){
                console.log('支付失败');
                WeixinJSBridge.call('closeWindow');
            } //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
        });
}