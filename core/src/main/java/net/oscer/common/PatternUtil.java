package net.oscer.common;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 *
 * @author MRCHENIKE
 * @create 2019-05-24 16:26
 **/
public class PatternUtil {

    /**
     * 检测网址
     */
    public static final Pattern CHECK_SITE = Pattern.compile("[a-zA-z]+://[^\\s]*");

    /**
     * 话题，如：#你喜欢哪个城市？#
     */
    public final static Pattern topic_pattern = Pattern.compile("#.+?#");
    /**
     * @某人
     */
    public final static Pattern referer_pattern = Pattern.compile("@([^@^\\s^:^,^;^'，'^'；'^>^<]{1,})");
    /**
     * @某人
     */
    public final static Pattern at_pattern = Pattern.compile("@([^@^\\s^:^,^;^'，'^'；'^>^<^&^\\]^\\[]{1,})");

    public final static Pattern words_pattern = Pattern.compile("[,，]+");
    public final static Pattern words_pattern2 = Pattern.compile("[,， ]+");
    /**
     * 非中文、大小写英文、阿拉伯数字正则
     */
    public final static Pattern symbol_pattern = Pattern.compile("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]");
    /**
     * 中文字符
     */
    public final static Pattern chinese_pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");

    public final static Pattern mobiler = Pattern.compile("^(((13[0-9])|(14[5,7])|(15([0-3]|[5-9]))|(17[0,6-8])|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
    public final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    public final static Pattern link = Pattern.compile("[a-zA-z]+://[^\\s]*");

    /**
     * 正则校验
     *
     * @param pattern
     * @param input
     * @return
     */
    public static boolean check(Pattern pattern, String input) {
        if (StringUtils.isBlank(input)) {
            return false;
        }
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
