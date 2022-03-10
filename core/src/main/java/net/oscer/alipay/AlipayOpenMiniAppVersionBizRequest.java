package net.oscer.alipay;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 小程序撤销审核-biz
 *
 * @author kz
 * @create 2021-09-07 14:23
 **/
public class AlipayOpenMiniAppVersionBizRequest {

    /**
     * 商家小程序审核中的版本号, 不传默认撤消正在审核中的版本
     */
    @JSONField(name = "app_version")
    private String appVersion;

    /**
     * 小程序投放的端参数，例如投放到支付宝钱包是支付宝端。默认支付宝端。
     * 支持：
     * com.alipay.alipaywallet:支付宝端；
     * com.alipay.iot.xpaas：支付宝IoT端
     */
    @JSONField(name = "bundle_id")
    private String bundleId;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
