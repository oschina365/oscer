package net.oscer.framework;

import util.DateUtil;
import util.URLConnectionUtil;

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

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(Math.round(5) * 1000);
            System.out.println(i);
        }
    }

    public static void speed_toutiao_kz() throws InterruptedException {
        String cookie = "excgd=0426; UM_distinctid=169f79830a615f-0e149ac4c2ac2e-2202a7b-38400-169f79830a812b; qh[360]=1; _ga=GA1.2.997258058.1578009281; sid_guard=38f058fd150bf793ae127123324c7077%7C1586665413%7C5184000%7CThu%2C+11-Jun-2020+04%3A23%3A33+GMT; uid_tt=cf71df9f0c3593232758c22cc0d9d82d; sid_tt=38f058fd150bf793ae127123324c7077; sessionid=38f058fd150bf793ae127123324c7077; learning_shelf_visited=1; WIN_WH=360_564; SLARDAR_WEB_ID=57325458385; install_id=1134326640806067; ttreq=1$0c81c82d98f1f3e828ace469f3aaeaeef8477575; odin_tt=764c784b494c445038466f7a5154445414ee5994facbb78d084f43ce4abb79a4e97108525d577c8cb264feb8cb3a0c82";
        speed_toutiao(URL_SPEED_TOUTIAO_KZ, cookie, UA_SPEED_TOUTIAO_KZ);
    }

    /**
     * 今日头条极速版签到
     */
    public static void speed_toutiao(String url, String cookie, String ua) throws InterruptedException {
        Thread.sleep(Math.round(150) * 1000);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        headerMap.put("User-Agent", ua);
        String str = URLConnectionUtil.sendPost(url, headerMap, null);
        System.out.println(str);
        System.out.println(String.format("今日头条极速版签到成功~,{}", DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS)));
    }
}
