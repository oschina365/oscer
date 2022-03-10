import com.alibaba.fastjson.JSONArray;
import net.oscer.common.URLConnectionUtil;
import net.oscer.config.provider.HttpTools;
import net.oscer.profit.SpeedToutiao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author MRCHENIKE
 * @create 2021-09-04 17:50
 **/
public class Fanxing {

    public static final String DOMAIN = "https://fanxing.zwztf.net/api";
    public static final List<String> urls = new ArrayList<String>() {{
        //add("/goods/app/goods/page?current=1&name=%E8%8B%B9%E6%9E%9C&size=10&userId");
        //add("/goods/app/goods/page?current=1&name=%E9%A6%99%E8%95%89&size=10&userId");
        //add("/goods/app/goods/page?current=1&name=%E7%81%AB%E8%85%BF&size=10&userId");
        add("http://www.guanggoo.com/");
        add("http://www.guanggoo.com/node/qna");
        add("http://www.guanggoo.com/node/lowshine");
        add("http://www.guanggoo.com/node/job");
        add("http://www.guanggoo.com/t/60597");
        add("http://www.guanggoo.com/?p=1294");
        add("http://www.guanggoo.com/?p=1293");
        add("http://www.guanggoo.com/?p=1292");
        add("http://www.guanggoo.com/?p=666");
        add("http://www.guanggoo.com/?p=7777");

        //add("http://oscer.net/");
        //add("http://oscer.net/uni/q/list");
        //add("http://oscer.net/tweet");
        //add("http://oscer.net/q/26");
    }};

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(100, 150, 1, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("tenant_code", "10000001");
        for (int i = 0; i < 9999999; i++) {
            urls.parallelStream().forEach(row -> {
                poolExecutor.execute(new exec(headerMap, row));
            });
        }
    }

    static class exec implements Runnable {
        Map<String, String> headerMap;
        String url;

        public exec(Map<String, String> headerMap, String url) {
            this.headerMap = headerMap;
            this.url = url;
        }

        @Override
        public void run() {
            String str = URLConnectionUtil.sendGet(( url), null, null);
            System.out.println(str);
        }
    }

}
