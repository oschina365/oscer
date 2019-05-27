package net.oscer.framework;

import net.oscer.beans.Remind;
import net.oscer.beans.User;
import net.oscer.dao.TopicDAO;
import net.oscer.dao.UserDAO;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.regex.Matcher;

import static net.oscer.common.PatternUtil.referer_pattern;
import static net.oscer.common.PatternUtil.topic_pattern;

/**
 * @author kz
 */
public class RegularUtils {

    public static void main(String[] args) {
        System.out.println(fixTopic("#java##spring##qwer# 德玛西亚", 3, 0, 5, true));
    }

    /**
     * 处理@某人情况，@Timor
     *
     * @param save_remind
     * @param remind_text
     * @param sender
     * @param type
     * @param obj_id
     * @param input
     * @param max_match_count
     */
    public static String fixAt(boolean save_remind, String remind_text, long sender, int type, long obj_id, String input, int max_match_count) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        if (max_match_count <= 0) {
            max_match_count = 99999;
        }
        StringBuilder html = new StringBuilder();
        int match_count = 0;
        int lastIdx = 0;
        Matcher matcher = referer_pattern.matcher(input);
        while (matcher.find()) {
            match_count++;
            String origin_str = matcher.group();

            if (match_count <= max_match_count) {
                String str = origin_str.substring(1, origin_str.length()).trim();

                User user = UserDAO.ME.getUser(str);

                if (user != null && user.getId() > 0L) {
                    html.append("<a href=\"" + UserDAO.ME.user_link(user.getId()) + "\" class=\"referer\" target=\"_blank\">@");
                    html.append(str.trim());
                    html.append("</a> ");

                    //保存提醒记录
                    if (save_remind) {
                        Remind.ME.saveRemind(remind_text, sender, type, obj_id, user.getId());
                    }
                } else {
                    html.append(origin_str);
                }

            } else {
                html.append(origin_str);
            }
            lastIdx = matcher.end();
        }
        html.append(input.substring(lastIdx));
        return html.toString();
    }

    /**
     * 处理话题，#你喜欢什么表情#
     *
     * @param input
     */
    public static String fixTopic(String input, long sender, long tweet_id, int max_match_count, boolean save_topic) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        if (max_match_count <= 0) {
            max_match_count = 99999;
        }

        Matcher matcher = topic_pattern.matcher(input);
        StringBuilder html = new StringBuilder();
        int match_count = 0;
        int lastIdx = 0;
        LinkedHashSet<String> topics = new LinkedHashSet();
        while (matcher.find()) {
            match_count++;
            String origin_str = matcher.group();
            if (match_count <= max_match_count) {
                origin_str = origin_str.substring(1, origin_str.length() - 1);
                if (tweet_id > 0L && save_topic) {
                    topics.add(origin_str);
                }
                html.append("<a href=\"" + LinkTool.getHost("root") + "/topic/" + origin_str + "\" class=\"tweet_topic\" target=\"_blank\">");
                html.append("#" + origin_str + "#");
                html.append("</a> ");

            } else {
                html.append(origin_str);
            }
            lastIdx = matcher.end();
        }
        html.append(input.substring(lastIdx));
        if (CollectionUtils.isNotEmpty(topics)) {
            TopicDAO dao = TopicDAO.ME;
            topics.stream().forEach(s -> {
                dao.save_topic(sender, tweet_id, s);
            });
        }
        return html.toString();

    }
}
