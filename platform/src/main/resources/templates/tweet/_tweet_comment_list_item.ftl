#if($!commentTweets && $!commentTweets.size()>0)
    #foreach($comment in $commentTweets)
        #set($comment_user = $User.getById($comment.user_id))
        #set($comment_reply_user = $User.getById($comment.reply_user_id))
        <div class="pl-box-wrap  ">
        <div class="pl-box-top">
            <div class="dp-list-box">
                <div class="dl-user ">
                    <ul>
                        <li><a href="/u/$!{comment_user.id}" target="_blank"><img src="$!{comment_user.headimg}"></a></li>
                    </ul>
                    <div class="one-pl-content">
                        <p class="content">
                            #if($!comment_user.nickname)#set($comment_user_uname = $comment_user.nickname)#else#set($comment_user_uname = $comment_user.username)#end
                            <span class="name" style="color: black;font-size: 18px;">$!{comment_user_uname}：</span>
                                <a href="/u/$!{comment_user.id}" target="_blank"></a>&nbsp;&nbsp;<span class="author-content" style="font-size: 14px;">
                                #if($comment_reply_user)
                                    #if($!comment_reply_user.nickname)#set($comment_reply_user_uname = $comment_reply_user.nickname)#else#set($comment_reply_user_uname = $comment_reply_user.username)#end
                                    回复：<a href="/u/${comment_reply_user.id}" target="_blank">$!{comment_reply_user_uname}</a>
                                #end
                                $!{comment.content}
                            </span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="pl-box-btm">
            <div class="article-type pull-right">
                <div class="icon-like-prompt"><i class="icon icon-like active"></i><span class="c1">+1</span></div>
                #*<div class="icon-no-like-prompt"><i class="icon icon-no-like active"></i><span class="c1">+1</span></div>*#
                <ul>
                    <li>$!{FormatTool.format_intell_time($!comment.insert_date)}</li>
                    <li class="js-icon-like" data-type="like"><i class="icon icon-like "></i><span class="like">0</span></li>
                    #*<li class="js-no-icon-like" data-type="no-like"><i class="icon icon-no-like "></i><span class="like">0</span></li>*#
                </ul>
            </div>
            <a onclick="showPTextArea(this)">
                <div class="btn-dp transition js-add-dp-box"><i class="icon icon-dp"></i>回复</div>
            </a>
            <div class="pl-form-box dp-article-box">
                <textarea class="form-control" placeholder="请文明回复~" maxlength="255" style="max-height: 120px;font-size: 16px;"></textarea>
                <button class="btn btn-primary btn-lg"
                        onclick="replyTweetComment(this,${tweet.id},'$!{comment.user_id}')">回复
                </button>
            </div>
        </div>
    </div>
    #end
#end