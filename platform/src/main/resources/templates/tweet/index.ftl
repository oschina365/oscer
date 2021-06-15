<#include "../layout/front/layout.ftl"/>
<@html title_="oscer社区">
    <link rel="stylesheet" href="/res/webupload/webupload.css">
    <style>
        .fly-footer {
            display: none;
        }
    </style>
    <div class="tweet-push" style="width: 79.9%;margin-left: 12.1%;margin-top: 2%">
        <textarea name="tweet_content" id="tweet_content" style="width: 100%;resize:none;font-size: 12px;"
                      placeholder="今天你动弹了吗" class="layui-textarea"></textarea>
        <div class="layui-btn-group" style="margin-top: 4px;">
            <a class="layui-btn layui-btn-warm layui-btn-sm" id="uploadImg"><i class="layui-icon">&#xe67c;</i>上传图片</a>
            <a class="layui-btn layui-btn-sm" style="background-color: #32aa66" onclick="push_tweet()"><i class="layui-icon">&#xe609;</i>弹</a>
        </div>
        <div id="uploader" class="wu-example">
            <div class="queueList filled" style="padding: 0px;">
                <ul class="filelist tweetIndexUl">
                </ul>
            </div>
            <div class="statusBar" style="height: 0px;display: none">
            </div>
        </div>
    </div>

    <div id="tweetAllList" class="tweetAllList">

    </div>



    <div id="hotTweetList" class="hotTweetList">
        <h2 style="background: #FFFFFF;padding-left: 10px;">热门动弹</h2>
        <#list hotTweets as view>
            <#assign tweet=view.tweet/>
            <#assign user=view.user/>
            <#assign hasPraise=view.gUserHasPraise/>
            <#assign tweet=view.tweet/>
            <#assign uname=user.nickname/>
            <div class="mod-b mod-art mod-white blog_selector" style="height: auto;">
                <div class="mob-ctt" style="width: 100%;float: none;">
                    <h3 style="word-wrap:break-word;">
                        <a style="cursor: pointer;color: #333;" href="/tweet/${tweet.id}" class="transition msubstr-row2">
                            ${tweet.content}
                        </a>
                    </h3>
                    <div class="mob-author">
                        <a href="/u/${user.id}" target="_blank"><img class="hour-tx" src="${user.headimg}" alt="头像"></a>
                        <a href="/u/${user.id}" target="_blank">
                            <span class="author-name " style="margin-left: 0px;">${uname}</span>
                        </a>
                        <span class="time">${view.insertDate}</span>
                        <i class="layui-icon layui-icon-read" title="阅读"></i><em>${tweet.view_count}</em>
                        <a href="/tweet/${tweet.id}" target="_blank" style="cursor: pointer;">
                            <i class="layui-icon layui-icon-reply-fill" title="评论"></i><em>${tweet.comment_count}</em>
                        </a>
                        <i class="layui-icon layui-icon-star-fill" title="收藏"></i><em>${tweet.collect_count}</em>
                        <a onclick="tweet_praise('${tweet.id}')">
                            <i class="layui-icon layui-icon-praise <#if hasPraise>like</#if>" title="点赞"></i>
                            <em>
                                <c id="tweetPraiseCount_${tweet.id}">${tweet.praise_count}</c>
                            </em>
                        </a>
                        &nbsp;<a href="/tweet/${tweet.id}" target="_blank" style="color: rgba(0,0,0,.5)">查看</a>
                    </div>

                    <#if tweet.thumbs??>
                        <div id="uploader" class="wu-example">
                            <div class="queueList filled" style="padding: 0px;">
                                <ul class="filelist">
                                    <#list view.thumbs as photo>
                                        <li>
                                            <a onclick="showImgs(this)">
                                                <p class="imgWrap">
                                                    <img src="${photo}" style="height: 100%"></p>
                                                <p class="progress"><span></span></p>
                                            </a>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </#if>
                    <div class="mob-sub"></div>
                </div>
            </div>
        </#list>
    </div>

    <div id="page" style="margin-left: 12.4%;"></div>

    <script id="tweetListTpl" type="text/html">
        {{#  if(d.list!=null&&d.list.length> 0){ }}
        {{#  layui.each(d.list, function(index, item){ }}

        {{#
        var tweet = item.tweet;
        var user = item.user;
        var hasPraise = item.gUserHasPraise;
        var uname = user.nickname;
        }}

        <div class="mod-b mod-art mod-white blog_selector" style="height: auto;">
            <div class="mob-ctt" style="width: 100%;float: none;">
                <h3 style="word-wrap:break-word;">
                    <a style="cursor: pointer;color: #333;" href="/tweet/${tweet.id}" class="transition msubstr-row2">
                        {{tweet.content}}
                    </a>
                </h3>
                <div class="mob-author">
                    <a href="/u/{{user.id}}" target="_blank"><img class="hour-tx" src="{{user.headimg}}" alt="头像"></a>
                    <a href="/u/{{user.id}}" target="_blank">
                        <span class="author-name " style="margin-left: 0px;">{{uname}}</span>
                    </a>
                    <span class="time">{{item.insertDate}}</span>
                    <i class="layui-icon layui-icon-read" title="阅读"></i><em>{{tweet.view_count}}</em>
                    <a href="/tweet/${tweet.id}" target="_blank" style="cursor: pointer;">
                        <i class="layui-icon layui-icon-reply-fill" title="评论"></i><em>{{tweet.comment_count}}</em>
                    </a>
                    <i class="layui-icon layui-icon-star-fill" title="收藏"></i><em>{{tweet.collect_count}}</em>
                    <a onclick="tweet_praise('{{tweet.id}}')">
                        <i class="layui-icon layui-icon-praise #if($hasPraise)like#end" title="点赞"></i>
                        <em>
                            <c id="tweetPraiseCount_${tweet.id}">{{tweet.praise_count}}</c>
                        </em>
                    </a>
                    &nbsp;<a href="/tweet/${tweet.id}" target="_blank" style="color: rgba(0,0,0,.5)">查看</a>
                </div>

                {{#  if(item.thumbs!=null&&item.thumbs.length> 0){ }}
                <div id="uploader" class="wu-example">
                    <div class="queueList filled" style="padding: 0px;">
                        <ul class="filelist">
                            {{#  layui.each(item.thumbs, function(index, thumb){ }}
                            <li>
                                <a href="/tweet/{{tweet.id}}" style="cursor: pointer" target="_blank">
                                    <p class="imgWrap">
                                        <img src="{{thumb}}" style="height: 100%"></p>
                                    <p class="progress"><span></span></p>
                                </a>
                            </li>
                            {{#  }); }}
                        </ul>
                    </div>
                </div>
                {{# }}}


                <div class="mob-sub"></div>
            </div>
        </div>

        {{#  }); }}
        {{#} else { }}
        <div class="fly-none">没有相关帖子</div>
        {{# }}}

    </script>

    <script>
        layui.config({
            version: "3.0.0"
            , base: '/res/mods/' //这里实际使用时，建议改成绝对路径
        }).use(['form', 'layer', 'jquery', 'laypage', 'laytpl', 'util','upload'], function () {
            var form = layui.form
                , layer = parent.layer === undefined ? layui.layer : parent.layer
                , laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery;

            dataList(1);

            //执行实例
            var uploadInst = layui.upload.render({
                elem: '#uploadImg' //绑定元素
                ,url: '/up/lay' //上传接口
                ,before(){
                    var e = logined();
                    console.log(logined());
                    if (e.code==1) {
                        layer.msg("请登录后重试", {icon: 5});
                        return;
                    }
                }
                ,done: function(res){
                    console.log(res);
                    if(res && res.code==0 && res.data.src){
                        $(".statusBar").show();
                        $(".tweetIndexUl").append('<li><p class="imgWrap">'+
                            '<img src="'+res.data.src+'"  style="height: 100%"></p>' +
                            '<p class="progress"><span></span>' +
                            '</p><div class="file-panel" style="height: 25px; overflow: hidden;background: none">' +
                            '<a onclick="delImg(this)"><span class="cancel">删除</span></a></div></li>');
                    }
                }
                ,error: function(){
                    //请求异常回调
                }
            });

            /**
             * 查询数据列表
             * @param number
             */
            function dataList(number) {
                var show = $("#show").val();
                $.ajax({
                    url: '/tweet/page',
                    method: 'post',
                    dataType: 'json',
                    data: {"number": number, "show": show},
                    success: function (data) {
                        console.log(data);
                        if (data && data.code == 1) {
                            var listData = {"list": data.result.data};
                            console.log(listData);
                            var getTpl = tweetListTpl.innerHTML, view = document.getElementById('tweetAllList');


                            laytpl(getTpl).render(listData, function (html) {
                                view.innerHTML = html;
                            });
                            form.render();

                            if (data.result.total > 0) {
                                if (number === 1) {
                                    //分页标签
                                    pageBar(data.result.total, 10);
                                }
                            }

                        }
                    }
                });
            }

            /**
             * 数据分页
             * @param count
             * @param limit
             */
            function pageBar(count, limit) {
                var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

                laypage.render({
                    elem: "page",
                    limit: limit,
                    count: count,
                    first: '首页',
                    last: '尾页',
                    theme: themes[parseInt(Math.random() * themes.length)],
                    layout: ['prev', 'page', 'next'],
                    jump: function (obj, first) {
                        if (!first) {
                            $("#number").val(obj.curr);
                            dataList(obj.curr);
                        }
                    }
                });
            }
        }).extend({
            fly: 'index'
        }).use('fly');
    </script>
</@html>