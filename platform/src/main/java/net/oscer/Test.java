package net.oscer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.oscer.common.DateUtil;
import net.oscer.common.URLConnectionUtil;
import net.oscer.db.CacheMgr;
import net.oscer.profit.SpeedToutiao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {

    public static void main(String[] args) throws IOException {
        String str = "{\"err_no\": 0, \"data\": {\"show_watch_video_task\": false, \"next_treasure_time\": 1587883651, \"new_excitation_ad\": {\"score_source\": 1, \"fixed\": false, \"ad_id\": 2, \"score_amount\": 31, \"task_id\": 188}, \"treasure_ui_status\": 2, \"current_time\": 1587883051, \"pre_ui_status\": 2, \"stop_ts\": 1588217626, \"share_amount\": 200, \"score_amount\": 1300, \"double_bonus\": false}, \"err_tips\": \"success\"}\n";
        System.out.printf(str);
        SpeedToutiao toutiao = JSONArray.parseObject(str,SpeedToutiao.class);
        System.out.printf(str);
        System.out.println(1587883651L * 1000);
        System.out.println(System.currentTimeMillis());
        //write();
        //ThreadPool();
        //tweet_pub("弹", "_user_behavior_=6924a789-8e01-48fc-8fd1-7b4f542a84a8; visit-gitee-stars-bts-14-190305=1; banner_osc_scr_zhych%200308=1; Hm_lvt_d237257153dcc02ba4647b448cbafcb8=1552275927; _ga=GA1.2.648698262.1552459971; bad_id8387c580-a888-11e5-bc38-bb63a4ea0854=a0848d21-455c-11e9-ad87-bbe88d56d4af; visit-detail-banner-ad-0304-shych=1; banner_osc_scr_zhych%200315=1; visit-detail-banner-ad-0320-szych=1; banner_osc_scr_zhych%200320=1; banner_osc_scr_zhych%200321=1; aliyungf_tc=AQAAAJ7oTWbIbgQAUzWJd//opsblfce6; Hm_lvt_a411c4d1664dd70048ee98afe7b28f0b=1554861742,1554889082,1554892660,1554947941; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; _reg_key_=teMXWQpcVTpCtNoNiJj9; oscid=gAK91cd8tb84ms5SeLwVQyiV3HsmQn%2BQHmKz7JfWP9l34Ovp6M2pl%2BZ%2FpZeMB2HfdsjW5wnh%2FogwV8kez%2Bqojxraos3gKoR%2FOHzJUqrecsy%2BAriBkMuy0SK4E0Ls6rfa9iqkPcKEua7LXrdAS5uPqP3n%2B5pE0GAJnFuJvvUfFi8%3D; Hm_lpvt_a411c4d1664dd70048ee98afe7b28f0b=1554963613; arp_scroll_position=0", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        //testLab8();
       /* runThreadUseInnerClass();
        runThreadUseLambda();
        test123();*/

        //String re=URLConnectionUtil.sendGet("https://gitee.com/v5/user?access_token=21ae4ea4cc2d9821b8411b52642406ae", null, null);
        //System.out.println(re);
       /* List<String> urls = Arrays.asList("http://my.oschina.com/u/3270380/blog/1839680","http://my.oschina.com/u/3270380/blog/1839681",
                "http://my.oschina.com/u/3270380/blog/1839677","http://my.oschina.com/u/3270380/blog/1839658");

        for(int i=0;i<100;i++){
            urls.stream().forEach(url->{
                Map<String,String> map = new HashMap<>();
                map.put("Referer",url);
                *//*map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36\n" + "X-Requested-With: XMLHttpReques");*//*
                map.put("Cookie","_user_behavior_=61ad24ee-8cbc-43fe-aa5a-b1062e9e42b9; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; _reg_key_=x1blXHq9R1ny75PXYUQd; arp_scroll_position=0; oscid=IEgC4alfhmN4%2BIPIgrI3PZk8ggPh2tvxfaGGBKxmARMipe1TsyRG55l9qv4eE01OkCj9ZRH9%2FYynQeqGHUFtgnqhLjQRdXeR9gTyaUACUt1YbSPfk9Sd2S4vNfmE6zofIKLQKo%2B3btKCRqVJjOmefw%3D%3D");
                URLConnectionUtil.sendGet("http://my.oschina.com/action/visit/blog",map,"id="+url.split("/")[url.split("/").length-1]);
            });
        }*/
        //String cookie = "excgd=0426; UM_distinctid=169f79830a615f-0e149ac4c2ac2e-2202a7b-38400-169f79830a812b; qh[360]=1; _ga=GA1.2.997258058.1578009281; sid_guard=38f058fd150bf793ae127123324c7077%7C1586665413%7C5184000%7CThu%2C+11-Jun-2020+04%3A23%3A33+GMT; uid_tt=cf71df9f0c3593232758c22cc0d9d82d; sid_tt=38f058fd150bf793ae127123324c7077; sessionid=38f058fd150bf793ae127123324c7077; learning_shelf_visited=1; WIN_WH=360_564; SLARDAR_WEB_ID=57325458385; install_id=1134326640806067; ttreq=1$0c81c82d98f1f3e828ace469f3aaeaeef8477575; odin_tt=764c784b494c445038466f7a5154445414ee5994facbb78d084f43ce4abb79a4e97108525d577c8cb264feb8cb3a0c82";
        //open_jinri(null,cookie,"Dalvik/2.1.0 (Linux; U; Android 7.1.1; 15 Build/NGI77B) NewsArticle/7.4.3 cronet/TTNetVersion:4df3ca9d 2019-11-25");

    }

    public static void open_jinri(String msg, String cookie, String ua) {
        String url = "http://i-hl.snssdk.com/score_task/v1/task/open_treasure_box/?rit=coin&use_ecpm=0&_request_from=web&iid=1134326640806067&device_id=57325458385&ac=wifi&mac_address=90%3AF0%3A52%3A58%3A9A%3A40&channel=lite_meizu&aid=35&app_name=news_article_lite&version_code=743&version_name=7.4.3&device_platform=android&ab_version=668907%2C668905%2C668906%2C1640299%2C1652857%2C668908%2C668903%2C668904%2C928942%2C1454796&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=z2&ab_feature=z1&abflag=3&ssmix=a&device_type=15&device_brand=Meizu&language=zh&os_api=25&os_version=7.1.1&uuid=866774030804994&openudid=c2a06ab4fbe2dbbf&manifest_version_code=7430&resolution=1080*1920&dpi=480&update_version_code=74304&_rticket=1587878596336&plugin_state=265515039&sa_enable=0&tma_jssdk_version=1.61.0.7&rom_version=25&cdid=000c5001-30df-4462-810b-9debe7cd83a9%20HTTP/1.1";
        String params = "msg=" + msg;
        Map<String, String> headerMap = new HashMap<>();
        /*headerMap.put("AppToken", "e13a2836ee096d6a17ba58543627409c");*/
        headerMap.put("Cookie", cookie);
        headerMap.put("User-Agent", ua);
        String str = URLConnectionUtil.sendPost(url, headerMap, null);
        System.out.println(str);
        System.out.println(String.format("动弹发布成功~,{}", DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS)));
    }

    public static void maopao() {
        Scanner input = new Scanner(System.in);
        int NUM = 5;
        int[] a = new int[NUM];
        System.out.println("请输入数字:");

        for (int i = 0; i < a.length; i++) {
            a[i] = input.nextInt();
        }

        System.out.println("排序之前:");
        for (int i : a) {
            System.out.print(i + "\t");
        }

        //冒泡排序实现
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }

        System.out.println("\n排序之后:");
        for (int i : a) {
            System.out.print(i + "\t");
        }
        System.out.print("\n");
    }

    public static void write() throws IOException {
        long t = System.currentTimeMillis();
        // 打开文件
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\a.txt"), StandardCharsets.UTF_8))) {
            IntStream.range(0, 9999 + 1) // 遍历
                    .mapToObj(String::valueOf) // 转为字符串
                    .map(s -> "0000".substring(0, "0000".length() - s.length()) + s + "\n") // 补0，换行
                    .forEach(s -> {
                        // 写入文件
                        try {
                            writer.write(s);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            writer.flush();
        }
        System.out.println((System.currentTimeMillis() - t) + "ms");
    }

    public static void ThreadPool() {
        long t = System.currentTimeMillis();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(100, 1000, 1, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
        long num = 100000L;

        poolExecutor.execute(new getURl("http://www.zhihuihuiwu.com/event/e614cff8-89c7-4bad-94cf-f889c7fbad86", num));

        poolExecutor.shutdown();
        while (poolExecutor.isTerminated()) {
            System.out.println((System.currentTimeMillis() - t) + ":ms");
        }
    }

    static class GoDown implements Runnable {
        Long num;

        public GoDown(Long num) {
            this.num = num;
        }

        @Override
        public void run() {
            for (long i = 1L; i <= num; i++) {
                CacheMgr.set("test", "" + i, i);
                System.out.println(CacheMgr.get("test", "" + i));
            }

        }
    }

    public static void tweet_pub(String msg, String cookie, String ua) {
        String url = "https://www.oschina.net/action/apiv2/tweet_pub";
        String params = "msg=" + msg;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("AppToken", "e13a2836ee096d6a17ba58543627409c");
        headerMap.put("Cookie", cookie);
        headerMap.put("User-Agent", ua);
        String str = URLConnectionUtil.sendPost(url, headerMap, params);
        System.out.println(str);
        System.out.println(String.format("动弹发布成功~,{}", DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS)));
    }

    static class getURl implements Runnable {
        String url;
        Long num;

        public getURl(String url, Long num) {
            this.url = url;
            this.num = num;
        }

        @Override
        public void run() {
            for (long i = 1L; i <= num; i++) {
                URLConnectionUtil.sendGet(url, null, null);
                System.out.println("第" + i + "次访问");
            }

        }
    }


    public static void testLab8(){
        long t0 = System.nanoTime();

        //初始化一个范围100万整数流,求能被2整除的数字，toArray（）是终点方法

        int a[]=IntStream.range(0, 1_000_000).filter(p -> p % 2==0).toArray();

        long t1 = System.nanoTime();

        //和上面功能一样，这里是用并行流来计算

        int b[]=IntStream.range(0, 1_000_000).parallel().filter(p -> p % 2==0).toArray();

        long t2 = System.nanoTime();

        //我本机的结果是serial: 0.06s, parallel 0.02s，证明并行流确实比顺序流快

        System.out.printf("serial: %.2fs, parallel %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9);

        List<Integer> list = Arrays.asList(1,2,3,4);
        list =list.stream().parallel().filter(n-> n%2==0).collect(Collectors.toList());
        System.out.println(list.toString());

    }

    public static void runThreadUseLambda() {
        //Runnable是一个函数接口，只包含了有个无参数的，返回void的run方法；
        //所以lambda表达式左边没有参数，右边也没有return，只是单纯的打印一句话
        new Thread(() ->System.out.println("lambda实现的线程")).start();
    }

    public static void runThreadUseInnerClass() {
        //这种方式就不多讲了，以前旧版本比较常见的做法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("内部类实现的线程");
            }
        }).start();
    }

    public static void test123(){
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);

        JButton button1 = new JButton("点我!");
        JButton button2 = new JButton("也点我!");

        frame.getContentPane().add(button1);
        frame.getContentPane().add(button2);
        //这里addActionListener方法的参数是ActionListener，是一个函数式接口
        //使用lambda表达式方式
        button1.addActionListener(e -> { System.out.println("这里是Lambda实现方式"); });
        //使用方法引用方式
        button2.addActionListener(Test::doSomething);
    }

    /**
     * 这里是函数式接口ActionListener的实现方法
     * @param e
     */
    public static void doSomething(ActionEvent e) {

        System.out.println("这里是方法引用实现方式");

    }

}
