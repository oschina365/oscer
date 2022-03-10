package net.oscer.sendcloud.common.vo;

import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date  2018-03-12
 * @author kz
 */
public class SendCloudSmsParamVO extends ResultVO implements Serializable {
    public Integer templateId;
    public Integer msgType = 0;
    public List<String> phone;
    public Map<String, String> vars;

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public List<String> getPhone() {
        return phone;
    }

    public String getPhoneString() {
        StringBuilder sb = new StringBuilder();
        for (String p : phone) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(p);
        }
        return sb.toString();
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public String getVarsString() {
        return JSONObject.fromObject(vars).toString();
    }

    public void addPhone(String mobile) {
        if (CollectionUtils.isEmpty(phone)) {
            phone = new ArrayList<String>();
        }
        phone.add(mobile);
    }

    public void addVars(String key, String value) {
        if (MapUtils.isEmpty(vars)) {
            vars = new HashMap<String, String>();
        }
        vars.put(key, value);
    }

    @Override
    public String toString() {
        return "SendCloudSmsParamVO{" +
                "templateId=" + templateId +
                ", msgType=" + msgType +
                ", phone=" + phone +
                ", vars=" + vars +
                '}';
    }
}