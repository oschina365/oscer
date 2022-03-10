#set($page_title='动弹广场')
#set($plugs =['<script src="/static/js/lifes/tweet.js"></script>',
    '<link rel="stylesheet" href="/static/css/lifes/webupload.css" />'
    ])
#static($plugs)

<div class="container" id="index" style="padding: 0 6px">
    <div class="wrap-left pull-left blog-list-wrap">
        <div class="tab-content">
            <div class="moder-rumors-list" style="display: flex;">
                <textarea name="tweet_content" id="tweet_content" style="width: 100%;resize:none;font-size: 12px;" placeholder="今天你动弹了吗" class="layui-textarea"></textarea>
            </div>
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
            <div class="mod-info-flow tab-pane fade in active" class="tab-pane fade in " id="viewTweetDataList">
                #invoke('tweets.vm')
                <div id="pagesView" align="center" style="display: none;">
                    <a id="nextView" href="/tweet/tweets?page=$pageIndex">下一页</a>
                </div>
                <br>
            </div>
        </div>
    </div>

    #invoke('tweet_hots.vm')
</div>