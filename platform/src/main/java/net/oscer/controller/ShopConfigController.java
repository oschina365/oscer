package net.oscer.controller;

import com.alipay.api.response.AlipayOpenMiniVersionListQueryResponse;
import net.oscer.beans.ShopConfig;
import net.oscer.common.ApiResult;
import net.oscer.dao.ShopConfigDAO;
import net.oscer.framework.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MRCHENIKE
 * @create 2021-10-09 11:21
 **/
@RequestMapping("/api/shopconfig/")
@RestController
public class ShopConfigController {

    @GetMapping("alipay")
    public ApiResult alipay() {
        List<ShopConfig> alipay = ShopConfigDAO.ME.alipay();
        return ApiResult.successWithObject(alipay);
    }

    @GetMapping("listQuery/{id}")
    public ApiResult listQuery(@PathVariable int id) {
        ShopConfig config = ShopConfigDAO.ME.get(id);
        if (config == null || StringUtils.isBlank(config.getConfig())) {
            return ApiResult.failWithMessage("支付宝配置不存在");
        }
        AlipayOpenMiniVersionListQueryResponse response = ShopConfigDAO.ME.miniVersionListQuery(config);
        if (!response.isSuccess()) {
            return ApiResult.failWithMessage("查询版本列表失败");
        }
        return ApiResult.successWithObject(response.getAppVersionInfos());
    }

    @PostMapping("apply/{id}")
    public ApiResult apply(@PathVariable int id) {
        ShopConfig config = ShopConfigDAO.ME.get(id);
        if (config == null || StringUtils.isBlank(config.getConfig())) {
            return ApiResult.failWithMessage("支付宝配置不存在");
        }
        AlipayOpenMiniVersionListQueryResponse response = ShopConfigDAO.ME.miniVersionListQuery(config);
        if (!response.isSuccess()) {
            return ApiResult.failWithMessage("查询版本列表失败");
        }
        String version = response.getAppVersionInfos().get(0).getAppVersion();
        boolean flag = ShopConfigDAO.ME.apply(config.getTenant_code(), ShopConfigDAO.ME.aliConfig(config), version);
        return flag ? ApiResult.success(String.format("版本号%s提交审核成功", version)) : ApiResult.failWithMessage("提交审核失败");
    }
}
