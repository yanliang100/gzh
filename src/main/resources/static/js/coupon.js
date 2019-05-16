$(function() {
    FastClick.attach(document.body);
});
$(document).on("click", ".aui-vou-btn-der-one", function() {
    var clickEle=$(this);
    var id=clickEle.parent().find("input[name='id']").val();
    $.post("recieve_coupon",{id:id},function(result){
        $.alert(result.message);
        if(result.code=="6000"){
            clickEle.removeClass("aui-vou-btn-der-one").text("已领取");
            var receive_amount=clickEle.parent().find("input[name='receive_amount']").val();
            var publish_amount=clickEle.parent().find("input[name='publish_amount']").val();
            var process=((receive_amount+1)*100/publish_amount);
            clickEle.parent().find("p").text("已抢"+process+"%");
            clickEle.parent().find(".aui-Speed").find(".aui-Speed-top").css("width",process+"%");
        }
    },"json");
});