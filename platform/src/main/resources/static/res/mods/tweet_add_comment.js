$(document).ready(function () {
    $(".dp-article-box").fadeOut(0);
    $(".js-add-dp-box,.js-hf-article-pl").click(function () {
        $(".dp-article-box").not($(this).next()).slideUp('fast');
        $(this).next().slideToggle(400);
    });

});

/**
 * 回复评论
 */
function replyTweetComment(obj,tweetId,replyUserId) {
    if(logined()){
        layui.use('layer', function () {
            var layer = layui.layer;

            var content = $(obj).parent().children('textarea').val();
            if(!content){
                layer.msg("回复内容不能为空",{icon:5});
                return;
            }
            $.ajax({
                url: '/tweet/add_comment',
                data:{"tweet_id":tweetId,"content":content,"reply_user_Id":replyUserId},
                type: 'post',
                dataType: 'json',
                success: function (d) {
                    if (d||d.code==1) {
                        layer.msg("回复成功~",{icon:6});
                        location.reload();
                    }else{
                        layer.msg(d.message?d.message:"回复失败",{icon:5});
                    }
                },error:function (d) {
                    layer.msg(d.message?d.message:"回复失败",{icon:5});
                }
            });
        });
    }else{
        showLogin();
        return;
    }
}

/**
 * 发表评论
 */
function addTweetComment(obj,tweetId) {
    if(logined()){
        layui.use('layer', function () {
            var layer = layui.layer;
            var content = $(obj).parent().children('textarea').val();
            if (!content) {
                layer.msg("评论内容不能为空",{icon:5});
                return;
            }
            $.ajax({
                url: '/tweet/add_comment',
                data: {"tweet_id":tweetId,"content": content},
                type: 'post',
                dataType: 'json',
                success: function (d) {
                    if (d||d.code == 1) {
                        layer.msg("评论成功~",{icon:6});
                        location.reload();
                    }else{
                        layer.msg(d.message?d.message:"评论失败",{icon:5});
                    }
                },error:function (d) {
                    layer.msg(d.message?d.message:"评论失败",{icon:5});
                }
            });
        });
    }else{
        showLogin();
        return;
    }
}

function showPTextArea(obj) {
    if($(obj).next('div').css("display")=='none'){
        $(obj).next().css({"display":"block"});

    }else{
        $(obj).next().css({"display":"none"});
    }
}