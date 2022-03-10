package net.oscer.alipay;

/**
 * 版本状态列表
 *
 * @author kz
 * @create 2021-09-07 14:02
 **/
public enum AlipayOpenMiniVersionStatusEnum {

    INIT("INIT", "开发中"),
    AUDITING("AUDITING", "审核中"),
    AUDIT_REJECT("AUDIT_REJECT", "审核驳回"),
    WAIT_RELEASE("WAIT_RELEASE", "待上架"),
    GRAY("GRAY", "灰度中"),
    RELEASE("RELEASE", "已上架"),
    OFFLINE("OFFLINE", "已下架"),
    AUDIT_OFFLINE("AUDIT_OFFLINE", "已下架");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    AlipayOpenMiniVersionStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
