//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.oscer.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiNiuApi {
    private static final long expires = 31536000L;
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String uploadToken;
    private static UploadManager uploadManager;
    public static QiNiuApi qiNiu;
    public static Auth auth;
    private static String bucket;
    private static BucketManager bucketManager;

    public QiNiuApi(String ak, String sk, String bucket) {
        auth = Auth.create(ak, sk);
        QiNiuApi.bucket = bucket;
        reload();
        uploadManager = new UploadManager(new Configuration());
        qiNiu = this;
    }

    public static void reload() {
        uploadToken = auth.uploadToken(bucket, (String)null, 31536000L, (StringMap)null);
    }

    public static void moveFile(String fromBucket, String fromFileKey, String toBucket, String toFileKey, boolean force) throws QiniuException {
        bucketManager.move(fromBucket, fromFileKey, toBucket, toFileKey, force);
    }

    public static void delete(String bucket, String key) throws QiniuException {
        bucketManager.delete(bucket, key);
    }

    public static JSONObject upload(File file) throws QiniuException {
        Response resp = uploadManager.put(file, file.getName(), uploadToken);
        return getResult(resp);
    }

    public static JSONObject uploadFileByte(byte[] fileByte, String fileKey) throws QiniuException {
        Response resp = uploadManager.put(fileByte, fileKey, uploadToken);
        return getResult(resp);
    }

    public static JSONObject getResult(Response response) throws QiniuException {
        JSONObject jo = new JSONObject();
        if (response.isOK()) {
            String result = response.bodyString();
            jo = JSONObject.parseObject(result);
            jo.put("result_code", "success");
        } else {
            jo.put("result_code", "error");
        }

        return jo;
    }
}
