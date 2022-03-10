package net.oscer.sendcloud.common.vo;

import java.io.Serializable;

/**
 * @date  2018-03-12
 * @author kz
 */
public class SendCloudVoiceParamVO implements Serializable {

	public String phone;
	public String code;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "SendCloudVoiceParamVO{" +
				"phone='" + phone + '\'' +
				", code='" + code + '\'' +
				'}';
	}
}