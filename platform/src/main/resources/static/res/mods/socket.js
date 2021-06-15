var ws_protocol = 'ws'; // ws 或 wss
var ip = '114.67.207.123'
var port = 84
var user = null;

var heartbeatTimeout = 5000; // 心跳超时时间，单位：毫秒
var reconnInterval = 1000; // 重连间隔时间，单位：毫秒

var binaryType = 'arraybuffer'; // 'blob' or 'arraybuffer';//arraybuffer是字节
var handler = new tiohandler()

var tiows;

function initWs() {
    var queryString = 't=tweet_group', param = "";
    tiows = new tio.ws(ws_protocol, ip, port, queryString, param, handler, heartbeatTimeout, reconnInterval, binaryType)
    tiows.connect()
}

function send(value) {
    tiows.send(value)
}

function clearMsg() {
    document.getElementById('contentId').innerHTML = ''
}

initWs();

$(function () {

    $.ajax({
        url: '/u/logined',
        type: 'post',
        async: false,
        dataType: 'json',
        success: function (d) {
            console.log(d);
            if (d.code == 1) {
                user = true;
                console.log(user);
            }
        }
    });
})

/**
 * 首页添加动弹
 * @param tweet
 */
function addTweet(result) {
    if (result) {
        var obj = $.parseJSON(result);
        var view = obj.result;
        var tweet = view.tweet, user = view.user;
        if (tweet && tweet.id > 0) {
            var name = user.username;
            if (user.nickname) {
                name = user.nickname;
            }

            var photos_html = "";
            if (tweet.thumbs) {
                for (var i in tweet.thumbs.split(",")) {
                    photos_html += '<li><a onclick="showImgs(this)"><p class="imgWrap">' +
                        '<img src="' + tweet.thumbs.split(",")[i] + '"style="height: 100%"></p>' +
                        '<p class="progress"><span></span></p></a></li>';
                }
            }

            $("#tweetIndexList").children().first().before('' +
                '<li>' +
                '   <div class="story-content">' +
                '       <div class="mt-story-title js-story-title" story-data-show="true">' +
                '           <ul class="hour-head">' +
                '               <li>' +
                '                   <a href="/u/' + user.id + '" target="_blank">' +
                '                   <img class="hour-tx" src="' + user.headimg + '" alt="头像"></a>' +
                '               </li>' +
                '               <li>' +
                '                   <a href="/u/' + user.id + '" target="_blank"><p style="font-size: 12px;">' + name + '</p></a>' +
                '                   <p>刚刚</p>' +
                '               </li>' +
                '           </ul>' +
                '       </div>' +
                '   <div class="mt-index-info-parent">' +
                '   <div class="story-info mt-story-info">' +
                '       <p class="story-detail-hide hour-detail-hide mt-index-cont mt-index-cont2 js-mt-index-cont2">' +
                '       <a href="/tweet/' + tweet.id + '" target="_blank" style="color: #0d1215;margin-left: -72%;">' + tweet.content + '</a>' +
                '       <br>' +
                '       <a onclick="tweet_praise("' + tweet.id + '")" style="cursor:hand;color: #bbb">' +
                '           <i class="layui-icon layui-icon-praise" title="点赞" style="margin-left: 60%;padding: 0 5px;"></i>' +
                '           <em>&nbsp;<c id="tweetPraiseCount_' + tweet.id + '">0</c></em>' +
                '       </a>' +
                '       <a href="/tweet/' + tweet.id + '" target="_blank" style="color: #bbb;padding: 0 5px;">' +
                '           <i class="layui-icon layui-icon-reply-fill" title="评论"></i><em>0</em>' +
                '       </a></p>' +
                '   </div>' +
                '</div></div></li>');
            $("#tweet_content").val("");
            $(".tweetIndexUl li").remove();
            $(".statusBar").hide();

            $("#tweetAllList").children().first().before(
                '<div class="mod-b mod-art mod-white blog_selector" style="height: auto;">' +
                '            <div class="mob-ctt" style="width: 100%;float: none;">' +
                '                <h3 style="word-wrap:break-word;">' +
                '                    <a style="cursor: pointer;color: #333;" href="/tweet/' + tweet.id + '" class="transition msubstr-row2">' +
                '                        ' + tweet.content +
                '                    </a>' +
                '                </h3>' +
                '                <div class="mob-author">' +
                '                    <a href="/u/' + user.id + '" target="_blank"><img class="hour-tx" src="' + user.headimg + '" alt="头像"></a>' +
                '                    <a href="/u/' + user.id + '" target="_blank">' +
                '                        <span class="author-name " style="margin-left: 0px;">' + name + '</span>' +
                '                    </a>' +
                '                    <span class="time">刚刚</span>' +
                '                    <i class="layui-icon layui-icon-read" title="阅读"></i><em>0</em>' +
                '                    <a href="/tweet/' + tweet.id + '" target="_blank" style="cursor: pointer;">' +
                '                        <i class="layui-icon layui-icon-reply-fill" title="评论"></i><em>0</em>' +
                '                    </a>' +
                '                    <i class="layui-icon layui-icon-star-fill" title="收藏"></i><em>0</em>' +
                '                    <a onclick="tweet_praise("' + tweet.id + '")">' +
                '                        <i class="layui-icon layui-icon-praise #if($hasPraise)like#end" title="点赞"></i>' +
                '                        <em>' +
                '                            <c id="tweetPraiseCount_' + tweet.id + '">0</c>' +
                '                        </em>' +
                '                    </a>' +
                '                    &nbsp;<a href="/tweet/"' + tweet.id + '" target="_blank" style="color: rgba(0,0,0,.5)">查看</a>' +
                '                </div>' +
                '                <div class="mob-sub"></div>' +
                '            </div>' +
                '        </div>'
            );
            $("#tweet_content").val("");
        }

    }

}


/**
 * 发送动弹
 */
function push_tweet() {
    layui.use('layer', function () {
        var layer = layui.layer;
        var content = $("#tweet_content").val();
        if (!logined()) {
            layer.msg("请登录后重试", {icon: 5});
            return;
        } else if (!content.trim()) {
            layer.msg("内容不能为空~~~", {icon: 5});
            return;
        } else if (!getCookie("tweet", 1000 * 20)) {
            layer.msg("动弹得太快了，休息下~~~", {icon: 5});
            return;
        } else {
            var lis = $(".tweetIndexUl li");
            var photos = [];
            if (lis && lis.length > 0) {
                lis.each(function () {
                    photos.push($(this).find("img").attr("src"))
                })
            }
            var uid = $.cookie("uid");
            content = "uid%@%" + uid + "%&%content%@%" + content;
            if (photos && photos.length > 0) {
                content = "uid%@%" + uid + "%&%content%@%" + content + "%&%photos%@%" + photos;
            }
            send(content);
            setCooike("tweet", "push");
        }
    })

}


function logined() {
    if (!user) {
        return false;
    }
    return true;
}

//封装过期控制代码
function setCooike(key, value) {
    var curTime = new Date().getTime();
    localStorage.setItem(key, JSON.stringify({data: value, time: curTime}));
}

function getCookie(key, exp) {
    var data = localStorage.getItem(key);
    if (!data) {
        return true;
    }
    var dataObj = JSON.parse(data);
    if (new Date().getTime() - dataObj.time > exp) {
        return true;
    } else {
        return false;
    }
}


