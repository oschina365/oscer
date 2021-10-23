package net.oscer.dao;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.FileItem;
import com.alipay.api.domain.RegionInfo;
import com.alipay.api.request.AlipayOpenMiniVersionAuditApplyRequest;
import com.alipay.api.request.AlipayOpenMiniVersionListQueryRequest;
import com.alipay.api.request.AlipayOpenMiniVersionOnlineRequest;
import com.alipay.api.response.AlipayOpenMiniVersionAuditApplyResponse;
import com.alipay.api.response.AlipayOpenMiniVersionListQueryResponse;
import com.alipay.api.response.AlipayOpenMiniVersionOnlineResponse;
import net.oscer.alipay.*;
import net.oscer.beans.ShopConfig;
import net.oscer.framework.FileUtils;
import net.oscer.framework.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kz
 * @create 2021-10-09 11:18
 **/
public class ShopConfigDAO extends CommonDao<ShopConfig> {

    public static final ShopConfigDAO ME = new ShopConfigDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public ShopConfig get(int id) {
        String sql = "select * from shop_config where id =?";
        return getDbQuery().read(ShopConfig.class, sql, id);
    }

    public List<ShopConfig> alipay() {
        String sql = "select * from shop_config where config_key='alipay_applet_config'";
        return getDbQuery().query(ShopConfig.class, sql);
    }


    public static void main(String[] args) {
        List<ShopConfig> alipay = ME.alipay();
        List<String> list = new ArrayList<String>() {{
            add("");
        }};
        /* for (ShopConfig item : alipay) {
         *//*if (!list.contains(item.getTenantCode())) {
                continue;
            }*//*
            AlipayOpenMiniVersionListQueryResponse response = miniVersionListQuery(item);
            if (response == null || CollectionUtils.isEmpty(response.getAppVersionInfos())) {
                continue;
            }
            AppVersionInfo info = response.getAppVersionInfos().get(0);
            if (info == null || !net.oscer.framework.StringUtils.equalsAnyIgnoreCase("init", info.getVersionStatus())) {
                continue;
            }
            apply(item.getTenant_code(), aliConfig(item), info.getAppVersion());
        }*/
    }

    public AlipayOpenMiniVersionListQueryResponse miniVersionListQuery(ShopConfig shopConfig) {
        AlipayBaseConfig conf = aliConfig(shopConfig);
        if (net.oscer.framework.StringUtils.isBlank(conf.getAppAuthToken())) {
            return null;
        }
        List<String> statusList = Stream.of(AlipayOpenMiniVersionStatusEnum.values()).
                map(AlipayOpenMiniVersionStatusEnum::getCode).collect(Collectors.toList());
        AlipayOpenMiniVersionListQueryRequest queryRequest = new AlipayOpenMiniVersionListQueryRequest();
        AlipayOpenMiniVersionListQueryBizRequest bizRequest = new AlipayOpenMiniVersionListQueryBizRequest();
        bizRequest.setBundleId("com.alipay.alipaywallet");
        bizRequest.setVersionStatus(StringUtils.join(statusList, ","));
        queryRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != conf.getAppAuthToken() && !"".equals(conf.getAppAuthToken())) {
            queryRequest.putOtherTextParam("app_auth_token", conf.getAppAuthToken());
        }
        AlipayOpenMiniVersionListQueryResponse response = (AlipayOpenMiniVersionListQueryResponse) new AlipayUtil().execute(queryRequest, conf, false);
        System.out.println(JSONObject.toJSONString(response));
        return response;
    }

    public boolean apply(String tenantCode, AlipayBaseConfig config, String version) {
        AlipayOpenMiniVersionAuditApplyRequest request = new AlipayOpenMiniVersionAuditApplyRequest();
        request.setAppVersion(version);
        request.setRegionType("CHINA");
        request.setVersionDesc("提交审核版本提交审核版本提交审核版本提交审核版本提交审核版本");
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.setProvinceCode("310000");
        regionInfo.setProvinceName("浙江省");
        regionInfo.setCityCode("310000");
        regionInfo.setCityName("杭州市");
        regionInfo.setAreaCode("311100");
        regionInfo.setAreaName("余杭区");
        request.setServiceRegionInfo(new ArrayList<RegionInfo>() {{
            add(regionInfo);
        }});
        request.setAppDesc("为商户提供领券的小程序，商户在支付宝运营中心配置的券，可以显示在此款小程序上，供消费者进行领取，然后线下支付，打开支付宝付款码核销领取的优惠券。");
        //零售电商_大型超市
        //request.setMiniCategoryIds("XS1020_XS2169");
        request.setAppSlogan("商户提供领券的小程序");
        //request.setAppLogo(new FileItem("D:\\works\\logo_gaitubao_280x280.png"));
        String first = "http://img.oscer.net/qiying/qiying_shop_first_screen.png";
        String second = "http://img.oscer.net/qiying/qiying_shop_second_screen.png";
        request.setFirstScreenShot(new FileItem(first, FileUtils.getFileStream(first)));
        request.setSecondScreenShot(new FileItem(second, FileUtils.getFileStream(second)));

        //request.setFirstLicensePic(new FileItem("D:\\works\\23_gaitubao_2160x3600.jpg"));

        //request.setFirstSpecialLicensePic(new FileItem("D:\\works\\24_gaitubao_2160x3600.jpg"));
        //request.setTestFileName(new FileItem("D:\\works\\审核辅助资料.rar"));
        request.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        AlipayOpenMiniVersionAuditApplyResponse execute = (AlipayOpenMiniVersionAuditApplyResponse) new AlipayUtil().execute(request, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("提交审核失败,商户号=%s", tenantCode));
            return false;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return true;
    }

    /**
     * 上架
     *
     * @param config
     */
    public void online(AlipayBaseConfig config, String version) {
        AlipayOpenMiniVersionOnlineRequest onlineRequest = new AlipayOpenMiniVersionOnlineRequest();
        AlipayOpenMiniAppVersionBizRequest bizRequest = new AlipayOpenMiniAppVersionBizRequest();
        bizRequest.setAppVersion(version);
        bizRequest.setBundleId("com.alipay.alipaywallet");
        onlineRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            onlineRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenMiniVersionOnlineResponse response = (AlipayOpenMiniVersionOnlineResponse) new AlipayUtil().execute(onlineRequest, config, false);
        ;
        System.out.println(JSONObject.toJSONString(response));
    }

    public AlipayBaseConfig aliConfig(ShopConfig shopConfig) {
        if (shopConfig == null || net.oscer.framework.StringUtils.isBlank(shopConfig.getConfig())) {
            return null;
        }
        JSONObject object = JSONObject.parseObject(shopConfig.getConfig());
        AlipayBaseConfig config = new AlipayBaseConfig();
        config.setAppId(object.getString("appId"));
        config.setPrivateKey(object.getString("privateKey"));
        config.setAlipayPublicKey(object.getString("alipayPublicKey"));
        config.setAppAuthToken(object.getString("appAuthToken"));
        return config;
    }
}
