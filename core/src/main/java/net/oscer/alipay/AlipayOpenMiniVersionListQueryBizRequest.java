package net.oscer.alipay;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 小程序版本列表查询-biz
 *
 * @author kz
 * @create 2021-09-07 14:09
 **/
public class AlipayOpenMiniVersionListQueryBizRequest {

    /**
     * 小程序投放的端参数，默认支付宝端 。
     * 支持：
     * com.alipay.alipaywallet：支付宝端
     * com.alipay.iot.xpaas：支付宝IoT端
     */
    @JSONField(name = "bundle_id")
    private String bundleId;

    /**
     * 版本状态列表，用英文逗号","分割，可选；不填默认不返回，
     * 状态可选值以及说明如下-
     * INIT: 开发中,
     * AUDITING: 审核中,
     * AUDIT_REJECT: 审核驳回,
     * WAIT_RELEASE: 待上架,
     * GRAY: 灰度中,
     * RELEASE: 已上架,
     * OFFLINE: 已下架,
     * AUDIT_OFFLINE: 已下架;
     * 见枚举类 -  AlipayOpenMiniVersionStatusEnum.java
     */
    @JSONField(name = "version_status")
    private String versionStatus;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = versionStatus;
    }
}
