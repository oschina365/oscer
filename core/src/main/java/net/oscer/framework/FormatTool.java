package net.oscer.framework;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static net.oscer.common.PatternUtil.*;

/**
 * 格式化工具
 *
 * @author kz
 */
public class FormatTool {

    private final static Whitelist qa_filter = Whitelist.basicWithImages();
    private final static Whitelist rpl_filter = Whitelist.basicWithImages();

    static {
        qa_filter.addTags("embed", "object", "param", "span", "pre", "code", "div", "h1", "h2", "h3", "h4", "h5", "h6", "table", "tbody", "tr", "th", "td", "s", "script", "hr", "emoji");
        qa_filter.addAttributes("script", "src");
        qa_filter.addAttributes("span", "style");
        qa_filter.addAttributes("pre", "class");
        qa_filter.addAttributes("code", "class");
        qa_filter.addAttributes("div", "class");
        qa_filter.addAttributes("a", "target", "name");
        qa_filter.addAttributes("table", "style", "border", "bordercolor", "cellpadding", "cellspacing");
        qa_filter.addAttributes("th", "style");
        qa_filter.addAttributes("td", "style");
        qa_filter.addAttributes("object", "width", "height", "classid", "codebase", "type");
        qa_filter.addAttributes("param", "name", "value");
        qa_filter.addAttributes("embed", "src", "quality", "width", "height", "allowfullscreen", "allowscriptaccess", "flashvars", "name", "type", "pluginspage", "align");
        qa_filter.addAttributes("p", "style");
        qa_filter.addAttributes("emoji", "class", "data-name", "data-emoji", "align");
        qa_filter.addAttributes("video", "controls", "id", "height", "poster", "width", "autoplay");
        qa_filter.addAttributes("source", "src", "type");

        rpl_filter.addTags("span", "pre", "div", "h1", "h2", "h3", "h4", "h5", "table", "tbody", "tr", "th", "td", "script", "hr", "emoji");
        rpl_filter.addAttributes("script", "src");
        rpl_filter.addAttributes("span", "style");
        rpl_filter.addAttributes("pre", "class");
        rpl_filter.addAttributes("div", "class");
        rpl_filter.addAttributes("a", "target", "name");
        rpl_filter.addAttributes("table", "border", "cellpadding", "cellspacing");
        rpl_filter.addAttributes("emoji", "class", "data-name", "data-emoji", "align");
    }

    /**
     * 对用户输入的问题和答案进行过滤
     *
     * @param body
     * @return
     */
    public final static String cleanBody(String body, boolean is_reply) {
        if (StringUtils.isBlank(body)) {
            return "";
        }
        //防止清理\n \r
        Document document = Jsoup.parse(body);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        //简书图片链接的处理
        Elements imgs = document.select("img");
        for (Element e : imgs) {
            String originalSrc = e.attr("data-original-src");
            if (StringUtils.isNotEmpty(originalSrc)) {//data-original-src有值的时候，再替换img的url
                e.attr("src", originalSrc);
            }
        }
        String context = document.html();
        body = Jsoup.clean(context, "", is_reply ? rpl_filter : qa_filter, new Document.OutputSettings().prettyPrint(false));
        //System.out.println("2 ----> " + body);
        Document doc = Jsoup.parseBodyFragment(body);
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        Elements scripts = doc.select("script");
        for (Element script : scripts) {
            String src = script.attr("src");
            if (!StringUtils.startsWithIgnoreCase(src, "http://runjs.cn/gist/"))
                script.remove();
            if (script.hasText())
                script.remove();
        }
        Elements elems = doc.getElementsByAttribute("style");
        for (Element elem : elems) {
            String style = elem.attr("style");
            if (StringUtils.contains(style, "javascript:") || StringUtils.contains(style, "http:"))
                elem.removeAttr("style");
            if (StringUtils.containsIgnoreCase(style, "position"))
                elem.removeAttr("style");
        }
        String res = HtmlUtils.filterEmbedSrc(doc.body().html());
        res = HtmlUtils.removeHtml(res);
        return res;
    }

    public static long rlong(String str) {
        if (StringUtils.isBlank(str) || !StringUtils.isNumeric(str)) {
            return 0L;
        }
        return Long.parseLong(str);
    }

    /**
     * 格式化评论输出
     *
     * @param text
     * @return
     */
    public static String comment(String text) {
        //内容清洗
        text = HtmlUtils.autoMakeLink(FormatTool.html(text), true, false);
        text = StringUtils.replace(text, "\r\n", "<br/>");
        text = StringUtils.replace(text, "\r", "<br/>");
        text = StringUtils.replace(text, "\n", "<br/>");
        return text;
    }

    public static String price(int price) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(price / 100.0);
    }

    /**
     * 格式化HTML文本
     *
     * @param content
     * @return
     */
    public static String text(String content) {
        if (content == null) {
            return "";
        }
        String html = StringUtils.replace(content, "<", "&lt;");
        return StringUtils.replace(html, ">", "&gt;");
    }

    /**
     * 格式化HTML文本
     *
     * @param content
     * @return
     */
    public static String html(String content) {
        if (content == null) {
            return "";
        }
        String html = content;
        html = StringUtils.replace(html, "'", "&apos;");
        html = StringUtils.replace(html, "\"", "&quot;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
        //html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }

    /**
     * 还原html文本
     *
     * @param content
     * @return
     */
    public static String reHtml(String content) {
        if (content == null) {
            return "";
        }
        String html = content;
        html = StringUtils.replace(html, "&apos;", "'");
        html = StringUtils.replace(html, "&quot;", "\"");
        html = StringUtils.replace(html, "&nbsp;&nbsp;", "\t");// 替换跳格
        html = StringUtils.replace(html, "&lt;", "<");
        html = StringUtils.replace(html, "&gt;", ">");
        return html;
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param maxLength
     * @return
     */
    public static String abbr_plaintext(String str, int maxLength) {
        return StringUtils.trim(abbr(plain_text(str), maxLength));
    }

    /**
     * 格式化HTML文本
     *
     * @param content
     * @return
     */
    public static String rhtml(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        String html = content;

        html = StringUtils.replace(html, "&", "&amp;");
        html = StringUtils.replace(html, "<", "&lt;");
        html = StringUtils.replace(html, ">", "&gt;");
        return html;
    }

    /**
     * 从一段HTML中萃取纯文本
     *
     * @param html
     * @return
     */
    public static String plain_text(String html) {
        return StringUtils.getPlainText(html);
    }

    /**
     * 字符串智能截断
     *
     * @param str
     * @param maxWidth
     * @return
     */
    public static String abbreviate(String str, int maxWidth) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return _Abbr(str, maxWidth);
    }

    public static String abbr(String str, int maxWidth) {
        return StringUtils.abbreviate(str, maxWidth);
    }

    private static String _Abbr(String str, int count) {
        if (str == null) {
            return null;
        }
        if (str.length() <= count) {
            return str;
        }
        StringBuilder buf = new StringBuilder();
        int len = str.length();
        int wc = 0;
        int ncount = 2 * count - 3;
        for (int i = 0; i < len; ) {
            if (wc >= ncount) {
                break;
            }
            char ch = str.charAt(i++);
            buf.append(ch);
            wc += 2;
            if (wc >= ncount) {
                break;
            }
            if (CharUtils.isAscii(ch)) {
                wc -= 1;
                //read next char
                if (i >= len) {
                    break;
                }
                char nch = str.charAt(i++);
                buf.append(nch);
                if (!CharUtils.isAscii(nch)) {
                    wc += 2;
                } else {
                    wc += 1;
                }
            }
        }
        buf.append("...");
        return buf.toString();
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean is_email(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        email = email.toLowerCase();
        if (email.endsWith("nepwk.com")) {
            return false;
        }
        if (email.endsWith(".con")) {
            return false;
        }
        if (email.endsWith(".cm")) {
            return false;
        }
        if (email.endsWith("@gmial.com")) {
            return false;
        }
        if (email.endsWith("@gamil.com")) {
            return false;
        }
        if (email.endsWith("@gmai.com")) {
            return false;
        }
        return emailer.matcher(email).matches();
    }

    public static boolean is_link(String slink) {
        if (StringUtils.isBlank(slink)) {
            return false;
        }
        return link.matcher(slink).matches();
    }


    public static String replace(String text, String repl, String with) {
        return StringUtils.replace(text, repl, with);
    }

    /**
     * 按给定的模式格式化数字
     * 如：$format.number($0.2345,'##.##%')返回23.45%
     *
     * @param number
     * @param pattern @see DecimalFormat.applyPattern()
     * @return
     */
    public static String number(double number, String pattern) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern(pattern);

        return df.format(number);
    }

    private final static Pattern phonePattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
    private final static Pattern qqPattern = Pattern.compile("^\\d{5,11}$");

    /**
     * 判断手机号码
     *
     * @param qq
     * @return
     */
    public static boolean isQQ(String qq) {
        if (StringUtils.isBlank(qq)) {
            return false;
        }
        return qqPattern.matcher(qq).matches();
    }

    /**
     * 判断手机号码
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        return phonePattern.matcher(number).matches();
    }

    public static boolean isMobile(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        return mobiler.matcher(number).matches();
    }

    /**
     * 过滤ascii码不可见字符(如果数据存在ascii码不可见字符，则客户端接口解析异常)
     * 其实属于ascii码中的控制字符，它们是0到31、以及127
     * 排除0、7-13（转义字符）
     *
     * @param content
     * @return
     */
    public static String invisibleAsciiFilter(String content) {
        if (content != null && content.length() > 0) {
            char[] contentCharArr = content.toCharArray();
            for (int i = 0; i < contentCharArr.length; i++) {
                if (contentCharArr[i] < 0x20) {
                    if (contentCharArr[i] != 0x00 && (contentCharArr[i] < 0x07 || contentCharArr[i] > 0x0D)) {
                        contentCharArr[i] = 0x20;
                    }
                } else if (contentCharArr[i] == 0x7F) {
                    contentCharArr[i] = 0x20;
                }
            }
            return new String(contentCharArr);
        }

        return "";
    }

    /**
     * 时间规则如下：
     * now和date做比对：
     * 1.now - date < 1分钟 return 刚刚
     * 2.now - date < 1个小时 return XX分钟前
     * 3.now - date < 12小时  return XX小时前
     * 4.day = 0 天 return 今天
     * 5.day = 1 天 return 昨天
     * 6.day = 2 天 return 前天
     * 7.其他 return yyyy/MM/dd 时间格式
     *
     * @param date
     * @return
     */
    public static String format_intell_time(Date date) {
        return date != null ? format_intell_timestamp(new Timestamp(date.getTime())) : null;
    }

    public static String format_intell_timestamp(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        Date date = new Date(ts.getTime());
        Date now = new Date();
        long ys = DateUtils.truncate(now, Calendar.YEAR).getTime();
        long today = DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime();
        long yesterday = today - 24L * 60 * 60 * 1000;
        long beforeYesterday = yesterday - 24L * 60 * 60 * 1000;
        long n = now.getTime();

        long e = date.getTime();

        if (e < ys) {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
        }
        if (e < beforeYesterday) {
            return new SimpleDateFormat("MM/dd HH:mm").format(date);
        }
        if (e < yesterday) {
            return new SimpleDateFormat("前天 HH:mm").format(date);
        }
        if (e < today) {
            return new SimpleDateFormat("昨天 HH:mm").format(date);
        }
        if (n - e > 60L * 60 * 1000) {
            return new SimpleDateFormat("今天 HH:mm").format(date);
        }
        if (n - e > 60 * 1000) {
            return (long) Math.floor((n - e) * 1d / 60000) + "分钟前";
        }
        if (n - e >= 0) {
            return "刚刚";
        }
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * 判断2个时间是否只间隔一天
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static boolean intervalOneDay(Date date1, Date date2, String pattern) {
        if (date1 == null || date2 == null) {
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String d1 = formatter.format(date1), d2 = formatter.format(date2);
        Integer dd1 = Integer.valueOf(d1), dd2 = Integer.valueOf(d2);
        return (dd1 - dd2 == 1) || (dd1 - dd2 == -1) ? true : false;
    }

    /**
     * 判断2个时间是否是同一天
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static boolean intervalSameDay(Date date1, Date date2, String pattern) {
        if (date1 == null || date2 == null) {
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String d1 = formatter.format(date1), d2 = formatter.format(date2);
        Integer dd1 = Integer.valueOf(d1), dd2 = Integer.valueOf(d2);
        return (dd1 - dd2 == 0) ? true : false;
    }

    private final static Pattern phonePatternSimple = Pattern.compile("^\\d{11}$");

    /**
     * 判断手机号码
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNumberSimple(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        return phonePatternSimple.matcher(number).matches();
    }

    public static boolean isPhone(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        return mobiler.matcher(number).matches();
    }

    private static long subTime(Date now, Date date) {
        long nowTime = now.getTime();
        long dateTime = date.getTime();
        return nowTime - dateTime;
    }

    public static String filterNumberK(Object obj) {
        long size = 0;
        if (obj != null) {
            if (obj instanceof Integer) {
                size = Long.valueOf((int) obj);
            }
            if (obj instanceof Long) {
                size = (long) obj;
            }
            if (size < 1000) {
                return size + "";
            }
            if (size / 1000 == 1) {
                return (size / 1000) + "K";
            }
            if (size / 1000 > 1) {
                Double sized = Double.valueOf(size) / 1000;
                Double result = Double.valueOf(String.format("%.1f", sized));
                if (result.intValue() - result == 0) {
                    return result.intValue() + "K";
                }
                return result + "K";
            }
            return "0";
        }
        return "0";
    }

    /**
     * 搜索关键字高亮显示
     *
     * @param key
     * @param text TODO 搜索高亮
     * @return
     */
    public static String highlight(String key, String text) throws Exception {
        return text;
    }

    /**
     * 将手机号码格式化为137****4567
     *
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return "";
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, phoneNumber.length());
    }

    /**
     * 对一些特殊的字符进行替换
     *
     * @param text 字符串
     * @return String
     */
    public static String specialCharReplace(String text) {
        return StringUtils.replace(text, "&nbsp;", "&amp;nbsp;");
    }

    /**
     * 自动为url生成链接
     *
     * @param text
     * @param only_lifes
     * @return
     */
    public static String auto_url(String text, boolean only_lifes) {
        return HtmlUtils.autoMakeLink(text, only_lifes, false);
    }

    /**
     * 检查所有输入是否有空值
     *
     * @param inputList
     * @return true:有空值；false:无空值
     */
    public static boolean isInputBlank(String... inputList) {
        for (String input : inputList) {
            if (StringUtils.isBlank(input)) {
                return true;
            }
        }
        return false;
    }

    public String[] list(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return StringUtils.split(text, ",");
    }

    /**
     * 保留白名单标签
     *
     * @param html
     * @return
     */
    public static String retainWhitelistHtml(String html) {
        return Jsoup.clean(html, Whitelist.relaxed().addAttributes("div", "class"));
    }

    /**
     * 清理无用标签
     *
     * @param html
     * @return
     */
    public static Document cleanhtml(String html) {
        String[] removeNodes = {"style", "script", "pre", "code", "link", "embed", "svg"};
        Document dom = Jsoup.parse(html);
        for (String node : removeNodes) {
            dom.select(node).remove();
        }
        return dom;
    }

    /**
     * 是否包含某字符串
     *
     * @param key
     * @param text
     * @return
     */
    public static boolean CheckContent(String key, String text) {
        //去掉 - _ | . , 及中文全角字符
        return !StringUtils.containsIgnoreCase(text, key);
    }


    public static String getSimpleContent(String txt){
        if(StringUtils.isEmpty(txt)){
            return txt;
        }
        String regEx = "[`~!\\-_@#$%^&*()+=|{}:;\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+『』$》《〖〗｛｝|{}【】‘；：”“’。，、？]";
        txt = txt.replaceAll(regEx, "");
        return checkSpecial(txt);
    }

    public static String checkSpecial(String txt){
        String pattern = "[\\t\\n\\f\\r\\p{Z}]";
        txt = txt.replaceAll(pattern,"");
        return txt;
    }

    /**
     * 文本处理
     *
     * @param content
     * @return
     */
    public static String fixContent(boolean save_remind, String remind_text, long sender, int type, long obj_id, String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        // 1. 基本处理,截断处理
        //content = StringUtils.abbreviate(FormatTool.text(content), 256);
        // 2. 超链接处理
        content = HtmlUtils.autoMakeLink(content, false, false);
        // 3. 提到某人的处理
        content = RegularUtils.fixAt(save_remind, remind_text, sender, type, obj_id, content, 10);

        content = RegularUtils.fixTopic(content, sender, 0L, 5, false);
        return content;
    }
}
