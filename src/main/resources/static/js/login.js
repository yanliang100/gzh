$(function() {
    FastClick.attach(document.body);
    $("#login").on("click", function () {

        var mobile = $("#mobile").val();
        var mobile_re = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
        if (!mobile_re.test(mobile)) {
            alert("请输入正确的手机号!")
            return;
        }

        var passwd = $("#passwd").val();
        var passwd_re = /^\w{6}$/;
        if(!passwd_re.test(passwd)){
            alert("密码长度为6位，只能包含字母、数字和下划线");
            return;
        }
        login(mobile,passwd);
    });
});

function login(mobile,passwd){
    $.ajax({
        type: "POST",
        async: false,
        dataType: "text",
        data: "mobile=" + mobile + "&passwd=" + passwd,
        url: "do_login",
        success: function (data) {
            var result=eval("("+data+")");
            $.toast(result.message);
            if(result.code=="8000")
            {
                setTimeout(function() {
                    window.location.href= "membercard";
                },1000);
            }
        }
    });
}
