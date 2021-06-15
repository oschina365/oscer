package net.oscer.view;


import net.oscer.beans.Blog;
import net.oscer.beans.Tweet;
import net.oscer.beans.User;
import net.oscer.dao.TweetDAO;
import net.oscer.framework.FormatTool;
import net.oscer.framework.LinkTool;
import net.oscer.framework.StringUtils;
import net.oscer.vo.UserVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TweetViewObject {

    public static final TweetViewObject ME = new TweetViewObject();


    public TweetViewObject() {

    }

    private Tweet tweet;
    private List<String> thumbs;
    private List<String> photos;
    private UserVO user;
    private Blog lastBlog;
    private int blogCount;
    private boolean gUserHasPraise;
    private String insertDate;


    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public Blog getLastBlog() {
        return lastBlog;
    }

    public void setLastBlog(Blog lastBlog) {
        this.lastBlog = lastBlog;
    }

    public int getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(int blogCount) {
        this.blogCount = blogCount;
    }

    public boolean isgUserHasPraise() {
        return gUserHasPraise;
    }

    public void setgUserHasPraise(boolean gUserHasPraise) {
        this.gUserHasPraise = gUserHasPraise;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public List<String> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public static List<TweetViewObject> tweetViewObjectList(User loginUser, String show, int page, int size) {
        return TweetDAO.ME.selectTweetsByShow(loginUser, show, page, size);
    }

    /**
     * 动弹详情页面
     *
     * @param tweet_id
     */
    public TweetViewObject(long tweet_id) {
        this.tweet = Tweet.ME.get(tweet_id);
        if (this.tweet != null) {
            this.user = UserVO.convert(User.ME.get(this.tweet.getUser_id()));
            if (StringUtils.isNoneBlank(this.tweet.getPhotos())) {
                this.setPhotos(Arrays.asList(FormatTool.list(this.tweet.getPhotos())));
            }
            if (StringUtils.isNoneBlank(this.tweet.getThumbs())) {
                this.setThumbs(Arrays.asList(FormatTool.list(this.tweet.getThumbs())));
            }
        }
    }
}
