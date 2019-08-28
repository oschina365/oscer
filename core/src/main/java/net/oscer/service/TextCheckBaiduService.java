package net.oscer.service;

import com.baidu.aip.contentcensor.AipContentCensor;
import net.oscer.beans.User;
import net.oscer.db.Entity;
import net.oscer.enums.BaiduCheckLabelEnum;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度文本审核
 * https://ai.baidu.com/docs#/TextCensoring-API/90baa678
 * 第二版，根据用户注册时间动态调整检测分值判断
 */
public abstract class TextCheckBaiduService {

    public static final int RESULT_FAILED = -1; //调用失败
    public static final int RESULT_OK = 0;      //正常内容
    public static final int RESULT_SPAM = 1;    //垃圾内容

    public final static long REG_TIME_1 = 1000L * 60 * 60 * 24;//注册时间一天
    public final static long REG_TIME_7 = REG_TIME_1 * 7;//注册时间7天
    public final static long REG_TIME_30 = REG_TIME_1 * 30;//注册时间1个月
    public final static long REG_TIME_90 = REG_TIME_1 * 90;//注册时间3个月

    public static final double PASS_SCORE = 0.75;//注册时间三个月内
    public static final double AD_PASS_SCORE_30 = 0.6;//恶意推广的分值界限，注册时间30天内
    public static final double AD_PASS_SCORE_7 = 0.5;//恶意推广的分值界限，注册时间7天内
    public static final double AD_PASS_SCORE_1 = 0.4;//恶意推广的分值界限，注册时间1天内

    public static final int AD_LABEL = 4;//恶意推广label值

    private static final String BAIDU_APP_ID = "14844111";
    private static final String BAIDU_API_KEY = "hgzBWypLthLDEmfgVIOzW19s";
    private static final String BAIDU_SECRET_KEY = "5gQDxp2ZUtRkNFp6FtZVOLyaHHwE31QK";

    private static final AipContentCensor baidu_client = new AipContentCensor(BAIDU_APP_ID, BAIDU_API_KEY, BAIDU_SECRET_KEY);

    /**
     * 检测内容的合法性
     *
     * @param content
     * @return -1 调用失败；0 正常； 1 包含垃圾内容  2：建议人工复审
     * @throws Exception
     */
    public abstract String checkText(long user, String content) throws Exception;

    /**
     * 使用百度云的文本审核接口
     *
     * @return
     */
    public static TextCheckBaiduService baidu() {
        return new TextCheckBaiduService() {
            @Override
            public String checkText(long user, String content) throws Exception {
                JSONObject json = baidu_client.antiSpam(content, null);
                if (!json.has("result")) {
                    //map.put(-1, "检测超时");
                    return null;
                }
                JSONObject result = json.getJSONObject("result");
                JSONArray reject = result.getJSONArray("reject");
                JSONArray pass = result.getJSONArray("pass");
                JSONArray review = result.getJSONArray("review");
                int label = hashHightScore(user, pass);
                if (label > 0) {
                    return BaiduCheckLabelEnum.map.get(label);
                }
                int spam = result.getInt("spam");
                if (spam == 0) {
                    return null;
                }
                if (spam == RESULT_SPAM) {
                    label = hashHightScore(user, reject);
                    if (label == 0) {
                        label = BaiduCheckLabelEnum.LABEL.L_99.getCode();
                    }
                    return BaiduCheckLabelEnum.map.get(label);
                }
                if (spam == 2) {
                    label = hashHightScore(user, reject);
                    if (label == 0) {
                        label = hashHightScore(user, review);
                        if (label == 0) {
                            label = BaiduCheckLabelEnum.LABEL.L_99.getCode();
                        }
                    }
                    return BaiduCheckLabelEnum.map.get(label);
                }
                return null;
            }
        };
    }

    /**
     * 分析百度接口返回的数据，score分值越高越表明文本内容存在垃圾
     *
     * @param user
     * @param pass
     * @return 返回label, 违禁值：1~6，正常：0
     */
    public int hashHightScore(long user, JSONArray pass) throws JSONException {
        if (pass == null || pass.length() <= 0) {
            return 0;
        }
        double s = PASS_SCORE;
        User login_user = User.ME.get(user);

        if ((System.currentTimeMillis() - login_user.getInsert_date().getTime()) < REG_TIME_1) {
            s = AD_PASS_SCORE_1;
        } else if ((System.currentTimeMillis() - login_user.getInsert_date().getTime()) < REG_TIME_7) {
            s = AD_PASS_SCORE_7;
        } else if ((System.currentTimeMillis() - login_user.getInsert_date().getTime()) < REG_TIME_30) {
            s = AD_PASS_SCORE_30;
        }
        for (int i = 0; i < pass.length(); i++) {
            JSONObject json = (JSONObject) pass.get(i);
            if (json == null || json.get("score") == null) {
                continue;
            }
            double socre = json.getDouble("score");
            int label = json.getInt("label");
            if (AD_LABEL == label && socre >= s) {
                return label;
            }
            if (socre <= PASS_SCORE) {
                continue;
            }
            return 0;
        }
        return 0;
    }

    public String messageError(JSONArray reject) throws JSONException {
        if (reject.length() > 0) {
            for (int i = 0; i < reject.length(); i++) {
                JSONObject json = (JSONObject) reject.get(i);
                JSONArray hit = json.getJSONArray("hit");
                if (hit != null && hit.length() > 0) {
                    return hit.get(0) != null ? hit.get(0).toString() : null;
                }
            }
        }
        return RESULT_SPAM + "";
    }

    /**
     * 使用示例
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TextCheckBaiduService baidu = baidu();
        for (int i = 0; i < 1; i++) {
            long ct = System.currentTimeMillis();
            String content = "代开发票找我哦，微信：1231231212";
            System.out.println(String.format("IDX:%s,RESULT:%s,TIME:%s\n", (i + 1), baidu.checkText(2, content), (System.currentTimeMillis() - ct)));
        }
    }


    /**
     * 记录百度文本检测失败的内容
     *
     * @param content
     */
    public static void writeFailRecord(String content) {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "error_record" + File.separator + "baidu_api_check_fail_record.txt";
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS") + ":>>> " + content);
            fw.write(System.getProperty("line.separator"));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
