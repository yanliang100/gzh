$(function() {
    FastClick.attach(document.body);
    wy_dele_click();
    openSaoyiSao();
});

function wy_dele_click(){
    $(document).on("click", ".wy-dele", function() {
        var $selected=$(this);
        $.confirm("您确定要把此商品从购物清单中删除吗?", "确认删除?", function() {
            $selected.closest(".weui-panel").remove();
            $.toast("商品已经删除!");
        }, function() {
            //取消操作
        });
    });
}

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
                jsApiList : ['scanQRCode' ]
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
                    putGoodCart(barCode);
                } else {
                    alert("请对准条形码扫码!");
                }
            }
        });
    });
}

function putGoodCart(barCode) {
    var $parent=$("#goods");
    $.post("get_brief_good",{barCode:barCode},function(data){
        var $node=$parent.find("[name='goodsSn'][tabindex='"+data.goodsSn+"']");
        if($node.length!=0){
            var $amount=$node.find(".Amount");
            $amount.val(Number($amount.val())+1);
        }else{
            $node=$parent.children(":first").clone();

            $node.attr("tabindex",data.goodsSn);
            $node.find("[name='name']").text(data.name);
            $node.find("[name='retailPrice']").text(data.retailPrice);
            $node.find("[name='primaryPicUrl']").attr("src",data.primaryPicUrl);
            $node.find(".Spinner").Spinner({value:1, len:3, max:999});
            $node.css("display","block");
            $parent.append($node);
        }
        $.confirm("是否继续扫描商品?", "确认?", function() {
            openSaoyiSao();
        }, function() {
            //计算总价
            countup();
        });
    },"json")
}

function countup(){
    var amount=0;
    $("#goods").children().each(function(i,e){
        if($(e).is(":visible")){
            var retailPrice=Number($(e).find("[name='retailPrice']").text());
            var count=Number($(e).find(".Amount").val());
            amount+=accMul(retailPrice,count);
        }
    });
    $("#zong1").text(amount);
}

function pay_qr_code(){
    location.href ="pay_qr_code?amount=" + $("#zong1").text();
}



//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果

function accMul(arg1,arg2){
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
//给Number类型增加一个mul方法，调用起来更加方便。
Number.prototype.mul = function (arg){
    return accMul(arg, this);
}
