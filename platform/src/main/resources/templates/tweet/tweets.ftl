

#if($!TweetViewLists && $!TweetViewLists.size()>0)
    #foreach($view in $TweetViewLists)
        #set($tweet = $view.tweet)
        #set($user = $view.user)
        #set($hasPraise = $view.gUserHasPraise)
        #if($!user.nickname)#set($uname = $user.nickname)#else#set($uname = $user.username)#end
        <div class="mod-b mod-art mod-white blog_selector" style="height: auto;">
            <div class="mob-ctt" style="width: 100%;float: none;">
                <h3 style="word-wrap:break-word;">
                    <a style="cursor: pointer;color: #333;" href="/tweet/$!{tweet.id}" class="transition msubstr-row2">
                        $!{tweet.content}
                    </a>
                </h3>
                <div class="mob-author">
                    <a href="/u/$!{user.id}" target="_blank"><img class="hour-tx" src="$!{user.headimg}" alt="头像"></a>
                    <a href="/u/$!{user.id}" target="_blank">
                        <span class="author-name " style="margin-left: 0px;">$!{uname}</span>
                    </a>
                    <span class="time">$!{FormatTool.format_intell_time($!{tweet.insert_date})}</span>
                   #* <i class="icon icon-read-count" title="阅读"></i><em>$!{tweet.view_count}</em>*#
                    <a href="/tweet/$!{tweet.id}" target="_blank" style="cursor: pointer;"><i class="icon icon-pinglun" title="评论"></i><em>$!{tweet.comment_count}</em></a>
                    #*<i class="icon icon-fvr" title="收藏"></i><em>$!{tweet.collect_count}</em>*#
                    <a onclick="tweet_praise($!{tweet.id})"><i class="icon icon-zan #if($hasPraise)like#end" title="点赞"></i><em><c id="tweetPraiseCount_$!{tweet.id}">$!{tweet.praise_count}</c></em></a>
                    &nbsp;<a href="/tweet/$!{tweet.id}" target="_blank" style="color: rgba(0,0,0,.5)">查看</a>
                </div>

                #if($!tweet.thumbs)
                    <div id="uploader" class="wu-example">
                        <div class="queueList filled" style="padding: 0px;">
                            <ul class="filelist">
                                #foreach($photo in $!FormatTool.list($!tweet.thumbs))
                                    <li>
                                        <a onclick="showImgs(this)">
                                            <p class="imgWrap">
                                                <img src="$!{photo}" style="height: 100%"></p>
                                            <p class="progress"><span></span></p>
                                        </a>
                                    </li>
                                #end
                            </ul>
                        </div>
                    </div>
                #end
                <div class="mob-sub"></div>
            </div>
        </div>
    #end
#end


<script>
    layui.config({
        version: "3.0.0"
        , base: '/res/mods/' //这里实际使用时，建议改成绝对路径
    }).use(['form', 'layer', 'jquery', 'laypage', 'laytpl', 'util'], function () {
        var form = layui.form
            , layer = parent.layer === undefined ? layui.layer : parent.layer
            , laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery;

        dataList(1);

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
                data: {"number": number,"show":show},
                success: function (data) {
                    console.log(data);
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.questions};
                        var getTpl = questionListTpl.innerHTML, view = document.getElementById('questionBodys');


                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();

                        if(data.result.count >0){
                            if (number === 1) {
                                //分页标签
                                pageBar(data.result.count, 10);
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