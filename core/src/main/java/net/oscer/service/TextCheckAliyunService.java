package net.oscer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import net.oscer.framework.StringUtils;

import java.util.*;

/**
 * @author kz
 * @date 2018/09/20
 * 文本检测
 */
public class TextCheckAliyunService extends TetxAntispamScanConfig {

    //成功标准
    public static final int success_code = 200;

    //连接超过时间
    public static final int connectTimeout = 1500;

    //审核时间
    public static final int readTimeout = 2000;

    public static TetxAntispamScanConfig.Result check(String content) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(getEndPointName(), regionId, "Green", getDomain());

        IAcsClient client = new DefaultAcsClient(profile);

        TextScanRequest textScanRequest = new TextScanRequest();
        // 指定api返回格式
        textScanRequest.setAcceptFormat(FormatType.JSON);
        // 指定请求方法
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST);
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(regionId);

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        task1.put("content", content);

        tasks.add(task1);

        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);

        textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        /**
         * 设置超时时间
         */
        textScanRequest.setConnectTimeout(connectTimeout);
        textScanRequest.setReadTimeout(readTimeout);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);

            //请求失败
            if (!httpResponse.isSuccess()) {
                return TetxAntispamScanConfig.success();
            }

            JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
            //返回code不是200
            if (scrResponse.getInteger("code") != success_code) {
                return TetxAntispamScanConfig.success();
            }
            JSONArray taskResults = scrResponse.getJSONArray("data");
            for (Object taskResult : taskResults) {
                if (success_code != ((JSONObject) taskResult).getInteger("code")) {
                    break;
                }
                JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                for (Object sceneResult : sceneResults) {
                    String label = ((JSONObject) sceneResult).getString("label");
                    String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                    //文本检测出违规
                    if (StringUtils.isNotEmpty(suggestion) && suggestion.equalsIgnoreCase(Suggestion.BLOCK.getCode())) {
                        return TetxAntispamScanConfig.fail(label);
                    }
                }
            }

        } catch (Exception e) {
            return TetxAntispamScanConfig.success();
        }
        return TetxAntispamScanConfig.success();
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(check("代开发票：V信【3.2.4.2.3.6.5.7】").toString());
        System.out.println(check("Full GC的触发条件\n" +
                "\n" +
                "（1）直接调用 System.gc() 时（调用后并不会立即发生 FGC，后面会在某个时间点发生），操作系统建议执行 Full GC(  -XX:+DisableExplicitGC 可禁用 )，但是不必然执行；\n" +
                "\n" +
                "（2）老年代的可用空间不足时；\n" +
                "\n" +
                "（3）方法区空间不足时，或 Metaspace Space 使用达到 MetaspaceSize 但未达到 MaxMetaspaceSize 阈值；大多情况下扩容都会触发；\n" +
                "\n" +
                "（4）concurrent mode failure ；\n" +
                "\n" +
                "（5）通过Minor GC后进入老年代的平均大小大于老年代的可用内存时。由 Eden 区、From Survior 区向 To Survior 区复制时，对象大小大于 To Survior 区可用内存，则把该对象转存到老年代，且老年代的可用内存小于该对象大小时；（ Promotion failed ）\n" +
                "\n" +
                "（6）执行 jmap -histo:live 或者 jmap -dump:live；\n" +
                "\n" +
                "\n" +
                "\n" +
                "注：一般 Full GC 会伴随一次 Minor GC。").toString());
    }
}
