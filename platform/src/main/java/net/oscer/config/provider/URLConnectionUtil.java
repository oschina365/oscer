package net.oscer.config.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLConnectionUtil {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, Map<String, String> headerMap, String params) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + params;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    conn.setRequestProperty(key, headerMap.get(key));
                }
            }

            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段

            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, Map<String, String> headerMap, String params) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    conn.setRequestProperty(key, headerMap.get(key));
                }
            }

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) {
        /*String url = "https://www.oschina.net/action/apiv2/tweet_pub";
        String params = "msg=弹";
        Map<String, String> headerMap=new HashMap<>();
        headerMap.put("AppToken","e13a2836ee096d6a17ba58543627409c");
        headerMap.put("Cookie", "user_1behavior_=781ab8ad-d167-428a-a8d4-137503a94027; oscid=7szTba8sfJTqp9EufssE61bWJLZ21QgJZLRWJCYiC4gG1Yt5mITtTW2j5n4jBBX%2FdsjW5wnh%2FohjCQLFkoVhnoHy5gayeJqVpxitCZ3Q0xrWKOdpJEx5Kd%2FZ1G%2FLQadSdWXvF6%2B26IpdnhUI7bUqgpxXH%2BOzt3Yu; Hm_lvt_a411c4d1664dd70048ee98afe7b28f0b=1532265696,1532311359,1532314340,1532397090; Hm_lvt_cb47adfe0fabd7059a2a90a495077efe=1532412488,1532412513,1532412616,1532414115; Hm_lpvt_cb47adfe0fabd7059a2a90a495077efe=1532414115");
        headerMap.put("User-Agent","android");
        String str = URLConnectionHelper.sendPost(url, headerMap,params);
        System.out.println(str);*/
       /* String blogUrl = "https://my.oschina.net/kezhen/?tab=newest&catalogId=0";
        Map<String, String> headerMap=new HashMap<>();
        headerMap.put("Cookie", "user_1behavior_=781ab8ad-d167-428a-a8d4-137503a94027; oscid=7szTba8sfJTqp9EufssE61bWJLZ21QgJZLRWJCYiC4gG1Yt5mITtTW2j5n4jBBX%2FdsjW5wnh%2FohjCQLFkoVhnoHy5gayeJqVpxitCZ3Q0xrWKOdpJEx5Kd%2FZ1G%2FLQadSdWXvF6%2B26IpdnhUI7bUqgpxXH%2BOzt3Yu; Hm_lvt_a411c4d1664dd70048ee98afe7b28f0b=1532265696,1532311359,1532314340,1532397090; Hm_lvt_cb47adfe0fabd7059a2a90a495077efe=1532412488,1532412513,1532412616,1532414115; Hm_lpvt_cb47adfe0fabd7059a2a90a495077efe=1532414115");
        String str = URLConnectionHelper.sendPost(blogUrl, headerMap,null);
        System.out.println(str);*/
    }
}