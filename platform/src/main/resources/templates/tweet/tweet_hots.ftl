#cache_html("html","www_tweet_hot")
<div class="wrap-right pull-right">
    #set($hots = $GlobalViewObject.tweetViewObject().tweetViewObjectList($g_user,"hot",1,5))
    <div class=" hot-article mod-white">
        <h3 style="font-weight: bold;">热门动弹</h3>
        <div id="fiveBlogListDataBody">
            #if($!hots && $!hots.size()>0)
                #foreach($h in $hots)
                    #set($tweet_hot = $h.tweet)
                    #set($user_hot = $h.user)
                    #set($hasPraise = $h.gUserHasPraise)
                    #if($!user_hot.nickname)#set($uname = $user_hot.nickname)#else#set($uname = $user_hot.username)#end
                    <div class="mod-b mod-art  blog_selector" style="height: auto;">
                        <div class="mob-ctt" style="width: 100%;float: none;">
                            <h3 style="word-wrap:break-word;">
                                <a style="cursor: pointer;color: #333;" href="/tweet/$!{tweet_hot.id}" class="transition msubstr-row2" target="_blank">
                                    $!{tweet_hot.content}
                                </a>
                            </h3>

                            <div>
                                <a href="/u/$!{user_hot.id}" target="_blank"><img class="hour-tx" src="$!{user_hot.headimg}" alt="头像"></a>
                                <a href="/u/$!{user_hot.id}" target="_blank" style="color: black;font-size: 12px;">
                                    <span class="author-name " >$!{uname}</span>
                                </a>
                                <span class="time">$!{FormatTool.format_intell_time($!{tweet_hot.insert_date})}</span>
                                <i class="icon icon-read-count" title="阅读"></i><em>$!{tweet_hot.view_count}</em>
                                <i class="icon icon-pinglun" title="评论"></i><em>&nbsp;$!{tweet_hot.comment_count}</em>
                                <i class="icon icon-zan #if($hasPraise)like#end" title="点赞"></i><em>&nbsp;&nbsp;$!{tweet_hot.praise_count}</em>
                            </div>
                            <br>
                            #if($!tweet_hot.thumbs)
                                <div id="uploader" class="wu-example">
                                    <div class="queueList filled" style="padding: 0px;">
                                        <ul class="filelist">
                                            #foreach($photo in $!FormatTool.list($!tweet_hot.thumbs))
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
        </div>
    </div>
</div>
#end