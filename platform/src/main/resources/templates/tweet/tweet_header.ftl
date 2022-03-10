        <!--动弹列表开始-->
        <div class="right-ad-box"></div>
        <div id="moment"></div>
        <div class=" moder-story-list ">
            <span class="pull-left project-more tweet-span" style="right: 19%;">
                <a class="transition green_color" target="_blank" onclick="home_tweets(this,'new')">最新动弹</a>
                <a class="transition " target="_blank" onclick="home_tweets(this,'hot')">热门动弹</a>
            </span>
            <span class="pull-right project-more story-more">
                <a href="/tweet" class="transition index-24-right " target="_blank">查看全部</a>
            </span>
            <span class="span-mark"></span>
            <div class="story-box-warp hour-box-warp" id="tweetListDataNew">
                <div class="nano">
                    <div class="overthrow nano-content description" tabindex="0"><br>
                        <ul class="box-list mt-box-list tweetListData" >
                            <#include "_tweet_list_item.ftl"/>
                        </ul>
                    </div>
                    <div class="nano-pane">
                        <div class="nano-slider" style="height: 179px; transform: translate(0px, 0px);"></div>
                    </div>
                </div>
            </div>
            <div class="story-box-warp hour-box-warp" id="tweetListDataHot" style="display: none;">
                <div class="nano">
                    <div class="overthrow nano-content description" tabindex="0"><br>
                        <ul class="box-list mt-box-list tweetListData" >
                            <#include "_tweet_list_hot.ftl"/>
                        </ul>
                    </div>
                    <div class="nano-pane">
                        <div class="nano-slider" style="height: 179px; transform: translate(0px, 0px);"></div>
                    </div>
                </div>
            </div>
            <div class="js-more-moment" data-cur_page="0"></div>
        </div>
        <br>
        <!--动弹列表结束-->