package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * 设置配置表
 *
 * @author kz
 * @email jmyinjg@163.com
 * @date 2021-07-07 20:15:05
 */
@Entity.Cache(region = "ShopConfig")
public class ShopConfig {

    private Integer id;

    /**
     * 商户号
     */
    private String tenant_code;

    /**
     * 门店ID
     */
    private String shop_id;

    /**
     * 配置项关键字
     */
    private String config_Key;

    /**
     * 配置值json
     */
    private String config;

    /**
     * 配置描述
     */
    private String config_desc;

    /**
     * 是否启用，0=禁用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 版本号
     */
    private Integer version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenant_code() {
        return tenant_code;
    }

    public void setTenant_code(String tenant_code) {
        this.tenant_code = tenant_code;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getConfig_Key() {
        return config_Key;
    }

    public void setConfig_Key(String config_Key) {
        this.config_Key = config_Key;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig_desc() {
        return config_desc;
    }

    public void setConfig_desc(String config_desc) {
        this.config_desc = config_desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
