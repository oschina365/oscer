package net.oscer.framework;

import com.alibaba.fastjson.JSONArray;
import net.oscer.common.DateUtil;
import net.oscer.common.URLConnectionUtil;
import net.oscer.db.CacheMgr;
import net.oscer.profit.SpeedToutiao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 收益
 *
 * @create 2020-04-26 14:05
 **/
public class Profit {

    public static final String URL_SPEED_TOUTIAO = "http://i-hl.snssdk.com/score_task/v1/task/open_treasure_box/";
    public static final String URL_SPEED_TOUTIAO_KZ = URL_SPEED_TOUTIAO + "?rit=coin&use_ecpm=0&_request_from=web&iid=1134326640806067&device_id=57325458385&ac=wifi&mac_address=90%3AF0%3A52%3A58%3A9A%3A40&channel=lite_meizu&aid=35&app_name=news_article_lite&version_code=743&version_name=7.4.3&device_platform=android&ab_version=668907%2C668905%2C668906%2C1640299%2C1652857%2C668908%2C668903%2C668904%2C928942%2C1454796&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=z2&ab_feature=z1&abflag=3&ssmix=a&device_type=15&device_brand=Meizu&language=zh&os_api=25&os_version=7.1.1&uuid=866774030804994&openudid=c2a06ab4fbe2dbbf&manifest_version_code=7430&resolution=1080*1920&dpi=480&update_version_code=74304&_rticket=1587878596336&plugin_state=265515039&sa_enable=0&tma_jssdk_version=1.61.0.7&rom_version=25&cdid=000c5001-30df-4462-810b-9debe7cd83a9%20HTTP/1.1";
    public static final String UA_SPEED_TOUTIAO_KZ = "Dalvik/2.1.0 (Linux; U; Android 7.1.1; 15 Build/NGI77B) NewsArticle/7.4.3 cronet/TTNetVersion:4df3ca9d 2019-11-25";

    public static final String URL_SPEED_TOUTIAO_SASA = URL_SPEED_TOUTIAO + "?rit=coin&use_ecpm=0&_request_from=web&iid=1134326640806067&device_id=57325458385&ac=wifi&mac_address=90%3AF0%3A52%3A58%3A9A%3A40&channel=lite_meizu&aid=35&app_name=news_article_lite&version_code=743&version_name=7.4.3&device_platform=android&ab_version=668907%2C668905%2C668906%2C1640299%2C1652857%2C668908%2C668903%2C668904%2C928942%2C1454796&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=z2&ab_feature=z1&abflag=3&ssmix=a&device_type=15&device_brand=Meizu&language=zh&os_api=25&os_version=7.1.1&uuid=866774030804994&openudid=c2a06ab4fbe2dbbf&manifest_version_code=7430&resolution=1080*1920&dpi=480&update_version_code=74304&_rticket=1587878596336&plugin_state=265515039&sa_enable=0&tma_jssdk_version=1.61.0.7&rom_version=25&cdid=000c5001-30df-4462-810b-9debe7cd83a9%20HTTP/1.1";
    public static final String UA_SPEED_TOUTIAO_SASA = "Dalvik/2.1.0 (Linux; U; Android 7.1.1; 15 Build/NGI77B) NewsArticle/7.4.3 cronet/TTNetVersion:4df3ca9d 2019-11-25";

    public static void main(String[] args) throws InterruptedException {
        System.out.println(stampToDate(1588217626 * 1000L));
    }

    public static void speed_toutiao_kz() throws InterruptedException {
        String cookie = "0426; UM_distinctid=169f79830a615f-0e149ac4c2ac2e-2202a7b-38400-169f79830a812b; qh[360]=1; _ga=GA1.2.997258058.1578009281; learning_shelf_visited=1; WIN_WH=360_564; install_id=1134326640806067; ttreq=1$0c81c82d98f1f3e828ace469f3aaeaeef8477575; passport_csrf_token=c7809735fea84422059e14085b15b8bb; d_ticket=d2abd2a3e49939b3abfcf71ac46155701ef97; sid_guard=0bedac91630b4452a5dd189693f4e71d%7C1587891793%7C5184000%7CThu%2C+25-Jun-2020+09%3A03%3A13+GMT; uid_tt=ef98639daa04da67810a666536d42ab0; sid_tt=0bedac91630b4452a5dd189693f4e71d; sessionid=0bedac91630b4452a5dd189693f4e71d; odin_tt=6b576367336965676e77354c504e73773cc43073d6b7de811e9a1adc7cafa5602ba735d3e95931c73d6584fe1fa6391a; SLARDAR_WEB_ID=57325458385";
        speed_toutiao("kz", URL_SPEED_TOUTIAO_KZ, cookie, UA_SPEED_TOUTIAO_KZ);
    }

    /**
     * 今日头条极速版签到
     */
    public static void speed_toutiao(String name, String url, String cookie, String ua) throws InterruptedException {
        if (!canOpen(name)) {
            return;
        }
        Thread.sleep(Math.round(8) * 1000);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        headerMap.put("User-Agent", ua);
        String str = URLConnectionUtil.sendPost(url, headerMap, null);

        SpeedToutiao toutiao = JSONArray.parseObject(str, SpeedToutiao.class);
        if (toutiao == null) {
            System.out.println("网络错误！");
            return;
        }
        if (toutiao.getErr_no() != 0 || toutiao.getData() == null) {
            System.out.println(toutiao.getErr_tips());
            return;
        }
        SpeedToutiao.DataBean data = toutiao.getData();
        //下次开启宝箱时间
        long next_treasure_time = data.getNext_treasure_time();
        //本地签到获得金币数量
        int score_amount = data.getScore_amount();

        CacheMgr.set("Profit", "speed_toutiao_" + name, next_treasure_time * 1000);

        String current_time = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS);
        System.out.println(String.format("今日头条极速版签到成功~  获得金币 %s 个,当前签到时间：%s ", score_amount, current_time));
    }

    public static boolean canOpen(String name) {
        Object o = CacheMgr.get("Profit", "speed_toutiao_" + name);
        if (o == null) {
            return true;
        }
        Long next_treasure_time = (Long) o;

        if (System.currentTimeMillis() > next_treasure_time) {
            return true;
        }
        return false;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        return stampToDate(Long.valueOf(s));
    }
}
