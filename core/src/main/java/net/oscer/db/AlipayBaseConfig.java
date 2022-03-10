package net.oscer.db;


/**
 * @author kz
 * @create 2021-08-23 10:19
 **/
public class AlipayBaseConfig {

    private String appId;

    private String privateKey;

    private String alipayPublicKey;

    private String appAuthToken;

    private String notifyUrl;

    private String appAes;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAppAes() {
        return appAes;
    }

    public void setAppAes(String appAes) {
        this.appAes = appAes;
    }
}
