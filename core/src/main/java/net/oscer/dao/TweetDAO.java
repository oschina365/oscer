package net.oscer.dao;

import net.oscer.beans.Praise;
import net.oscer.beans.Remind;
import net.oscer.beans.Tweet;
import net.oscer.beans.User;
import net.oscer.db.CacheMgr;
import net.oscer.db.Entity;
import net.oscer.enums.TweetEnum;
import net.oscer.enums.TypePraiseEnum;
import net.oscer.framework.FormatTool;
import net.oscer.framework.RegularUtils;
import net.oscer.framework.ResourceUtils;
import net.oscer.framework.StringUtils;
import net.oscer.view.TweetViewObject;
import net.oscer.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.tio.http.common.HttpRequest;

import java.util.*;

public class TweetDAO extends CommonDao<Tweet> {

    public static final TweetDAO ME = new TweetDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 首页查询动弹列表
     *
     * @param show
     * @param page
     * @param size
     * @return
     */
    public List<Long> selectTweetIdsByShow(String show, int page, int size) {
        String sql = "select id from tweets where status=1 " + TweetEnum.show_sql_map.get(show);
        if (show.equalsIgnoreCase(TweetEnum.HOME_SHOW.ALL.getCode())) {
            sql = "select id from tweets where status=1 order by id desc";
            return getDbQuery().query_slice_cache(Long.class, Tweet.ME.CacheRegion(), show, 50, sql, page, size);
        }
        if (show.equalsIgnoreCase(TweetEnum.HOME_SHOW.HOT.getCode())) {
            return getDbQuery().query_cache(Long.class, false, Tweet.CACHE_HOT_TWEET_IDS, "all", sql);
        }
        return getDbQuery().query_cache(Long.class, false, Tweet.ME.CacheRegion(), show, sql);
    }

    public int total() {
        String sql = "select count(*) from tweets where status=1";
        return getDbQuery().stat_cache(Tweet.ME.CacheRegion(), "total", sql);
    }

    public List<TweetViewObject> selectTweetsByShow(User loginUser, String show, int page, int size) {
        List<Long> ids = selectTweetIdsByShow(show, page, size);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<Tweet> tweets = Tweet.ME.loadList(ids);
        if (CollectionUtils.isEmpty(tweets)) {
            return null;
        }
        Set<Long> users = new TreeSet<>();
        List<TweetViewObject> viewObjectList = new LinkedList<>();
        Map<Long, User> mapUser = new HashMap<>();
        Map<Long, Boolean> mapPraise = new HashMap<>();
        tweets.stream().forEach(t -> {
            if (t.notNullObject(t)) {
                users.add(t.getUser_id());
                mapPraise.put(t.getId(), false);
            }
        });

        List<User> userList = User.ME.loadList(new ArrayList<>(users));

        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }

        userList.stream().forEach(u -> {
            if (u != null) {
                mapUser.put(u.getId(), u);
            }
        });

        if (loginUser != null && loginUser.getId() > 0L) {
            List<Praise> praises = PraiseDAO.ME.listByTypeUserIdBlogIds(TypePraiseEnum.VALUE.TWEET.getKey(), loginUser.getId(), ids);
            if (CollectionUtils.isNotEmpty(praises)) {
                praises.stream().filter(p -> p != null && p.getId() > 0L && p.getStatus() == Praise.PRAISE_YES).forEach(
                        p -> {
                            mapPraise.put(p.getObj_id(), true);
                        }
                );
            }
        }


        tweets.stream().forEach(t -> {
            if (t != null) {
                TweetViewObject object = new TweetViewObject();
                object.setTweet(t);
                User u = mapUser.get(t.getUser_id());
                object.setUser(UserVO.convert(u));
                object.setgUserHasPraise(mapPraise.get(t.getId()));
                viewObjectList.add(object);
                object.setInsertDate(FormatTool.format_intell_time(t.getInsert_date()));
                if (StringUtils.isNoneBlank(t.getPhotos())) {
                    object.setPhotos(Arrays.asList(FormatTool.list(t.getPhotos())));
                }
                if (StringUtils.isNoneBlank(t.getThumbs())) {
                    object.setThumbs(Arrays.asList(FormatTool.list(t.getThumbs())));
                }
            }
        });

        return viewObjectList;

    }

    /**
     * 查找用户正常且显示的动弹
     *
     * @param user_id
     * @return
     */
    public List<Tweet> userAllShowTweets(long user_id, int page, int size) {
        if (user_id <= 0) {
            return null;
        }
        String sql = "select id from tweets where user_id=? and status=1 order by id desc";
        List<Long> ids = getDbQuery().query_slice(long.class, sql, page, size, user_id);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return Tweet.ME.loadList(ids);
    }

    /**
     * 发表一条动弹
     *
     * @param httpRequest
     * @param user_id
     * @param map
     * @return
     */
    public Tweet push(HttpRequest httpRequest, long user_id, Map map) {
        Tweet tweet = new Tweet();
        if (map == null || map.get("content") == null) {
            return null;
        }
        String content = FormatTool.fixContent(false, "", user_id, Remind.TYPE_TWEET, 0, (String) map.get("content"));
        tweet.setContent(content);
        tweet.setUser_id(user_id);
        if (map.get("photos") != null) {
            tweet.setThumbs(map.get("photos").toString());
            tweet.setPhotos((map.get("photos").toString()).replaceAll("_thumb", ""));
        }
        tweet.save();
        RegularUtils.fixTopic(content, user_id, tweet.getId(), 5, true);
        FormatTool.fixContent(true, ResourceUtils.template("remind_tweet_refer", tweet.getId(), tweet.getId()), user_id, Remind.TYPE_TWEET, tweet.getId(), (String) map.get("content"));
        return tweet;
    }

    /**
     * 用户查看自己的动弹数量
     *
     * @param user_id
     * @param keyword
     * @param status
     * @return
     */
    public int userAllTweetCount(long user_id, String keyword, int status) {
        if (user_id <= 0) {
            return 0;
        }
        StringBuffer sql = new StringBuffer("select count(*) from tweets where user_id=? ");
        if (status < -1) {
            sql.append("and status in (-1,1) ");
        } else {
            sql.append("and status =").append(status).append(" ");
        }
        if (StringUtils.isNotBlank(keyword)) {
            sql.append("and content like '%").append(keyword).append("%' ");
        }
        sql.append("order by id desc");
        return getDbQuery().stat(sql.toString(), user_id);
    }


    /**
     * 用户查看自己的动弹列表
     *
     * @param user_id
     * @param page
     * @param size
     * @param keyword
     * @param status
     * @return
     */
    public List<Tweet> userAllTweets(long user_id, int page, int size, String keyword, int status) {
        if (user_id <= 0) {
            return null;
        }
        StringBuffer sql = new StringBuffer("select id from tweets where user_id=?");
        if (status < -1) {
            sql.append(" and status in (-1,1)");
        } else {
            sql.append(" and status =").append(status);
        }
        if (StringUtils.isNotBlank(keyword)) {
            sql.append(" and content like '%").append(keyword).append("%'");
        }
        sql.append(" order by id desc");
        List<Long> ids = getDbQuery().query_slice(long.class, sql.toString(), page, size, user_id);
        return Tweet.ME.loadList(ids);
    }

    /**
     * 查询一条正常显示的动弹
     *
     * @param tweet_id
     * @return
     */
    public Tweet selectShow(long tweet_id) {
        if (tweet_id <= 0L) {
            return null;
        }
        String sql = "select id from tweets where id=? and status=1";
        return Tweet.ME.getById(Tweet.ME, sql, tweet_id);
    }
}
