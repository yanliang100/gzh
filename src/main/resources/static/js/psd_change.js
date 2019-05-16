$(function() {
    FastClick.attach(document.body);
    $("#psdChangeClick").on("click", function () {

        var passwd = $("#passwd").val();
        var passwd_re = /^\w{6}$/;
        if(!passwd_re.test(passwd)){
            alert("密码长度为6位，只能包含字母、数字和下划线");
            return;
        }

        var mobile = $("#mobile").val();
        var mobile_re = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
        if (!mobile_re.test(mobile)) {
            alert("请输入正确的手机号!")
            return;
        }

        var telcx = $("#telcx").val();
        var telcx_re = /^\d{6}$/;
        if (!telcx_re.test(telcx)) {
            alert("验证码错误!");
            return;
        }

        //确认修改
        psdChangeMethord(passwd);
    });
    $("#btnFind").on("click", function () {
        var mobile = $("#mobile").val();
        var mobile_re = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
        if (!mobile_re.test(mobile)) {
            alert("请输入正确的手机号!")
            return;
        }
        //开始发送手机验证
        alertVerifyCodeMethod();
    });
});

var countdown=60;
function alertVerifyCodeMethod() {
    var disabled = $("#btnFind").hasClass("ui-disabled");
    if(disabled){
        return false;
    }
    var mobile=$("#mobile").val();
    if(mobile == "" || isNaN(mobile) || mobile.length != 11 ){
        alert("请填写正确的手机号！");
        return false;
    }
    settime();
    $.ajax({
        async:false,
        type: "POST",
        url: "sendsms",
        data: {"mobile":$("#mobile").val()},
        dataType: "json",
        async:false,
        success:function(data){
            var result=eval("("+data+")");
            alert(result.message);
        },
        error:function(err){
            console.log(err);
        }
    });
    function settime() {
        if (countdown == 0) {
            $("#btnFind").removeClass("ui-disabled");
            $("#btnFind").html("获取验证码");
            countdown = 60;
            return false;
        } else {
            $("#btnFind").addClass("ui-disabled");
            $("#btnFind").html("重新发送(" + countdown + "s)");
            countdown--;
        }
        setTimeout(function() {
            settime();
        },1000);
    }
}

function psdChangeMethord(passwd) {
    $.ajax({
        type: "POST",
        async: false,
        dataType: "text",
        data: "passwd=" + passwd,
        url: "do_psd_change",
        success: function (data) {
            var result=eval("("+data+")");
            $.toast(result.message+",请从会员卡菜单重新登录!");
            if(result.code=="7000")
            {
                setTimeout(function() {
                    WeixinJSBridge.call('closeWindow');
                },1000);
            }
        }
    });
}
