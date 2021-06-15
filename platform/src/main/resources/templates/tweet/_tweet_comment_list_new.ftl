#set($pageIndex = $req.param('page', 1))  ## 当前页码
#set($pageSize = 10)
#set($type = "new")
#set($commentTweets = $CommentTweet.selectListByType($tweet.id,$type,$pageIndex,$pageSize))
#invoke('_tweet_comment_list_item.vm')
<div id="pagesCollect" align="center" style="display: none;">
    <a id="nextCollect" href="/tweet/_tweet_comment_list_default?type=$type&page=$pageIndex">下一页</a>
</div>
