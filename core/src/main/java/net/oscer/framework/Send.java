package net.oscer.framework;

import com.forever7776.sendcloud.core.service.SendCloudService;
import com.forever7776.sendcloud.core.util.ResponseData;
import com.forever7776.sendcloud.remote.common.vo.ResultVO;
import com.forever7776.sendcloud.remote.common.vo.SendCloudEmailParamVO;
import enums.ResultEnum;
import net.oscer.beans.SendEmailTemplate;
import net.oscer.dao.SendEmailTemplateDAO;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

public class Send {

    /**
     * 构造发送邮件参数VO
     *
     * @param from
     * @param emails
     * @param type
     * @return
     */
    public static SendCloudEmailParamVO createSendEmailVo(String from, String emails, String type) {
        SendCloudEmailParamVO vo = new SendCloudEmailParamVO();

        vo.setCode(ResultEnum.STATUS.FAIL.getCode());
        if (StringUtils.isEmpty(type)) {
            vo.setMsg("邮件模板类型为空~~~~");
            return vo;
        }
        if (StringUtils.isEmpty(emails)) {
            vo.setMsg("收件人邮箱为空~~~~");
            return vo;
        }
        if (StringUtils.isEmpty(from)) {
            from = LinkTool.getHost("sendcloud_from");
        }

        SendEmailTemplate emailTemplate = SendEmailTemplateDAO.ME.selectByType(type);
        if (emailTemplate == null) {
            vo.setMsg("邮件模板为空");
            //sendSimpleMail("305389431@qq.com", "邮件模板为空提示", "邮件模板类型:" + type);
            return vo;
        }

        vo.setFrom(from);
        vo.setReplyTo(LinkTool.getHost("sendcloud_replyto"));
        vo.setSubject(emailTemplate.getTitle());
        vo.setContent(emailTemplate.getContent());
        vo.setTo(Arrays.asList(emails));
        vo.setCode(ResultEnum.STATUS.SUCCESS.getCode());
        return vo;
    }

    /**
     * 发送邮件
     *
     * @param vo
     * @return
     */
    public static ResultVO sendEmail(SendCloudEmailParamVO vo) throws Exception {
        if (vo.getPlaceholders() != null) {
            for (Map.Entry<String, String> entry : vo.getPlaceholders().entrySet()) {
                vo.setSubject(vo.getSubject().replaceAll("\\" + entry.getKey(), entry.getValue()));
                vo.setContent(vo.getContent().replaceAll("\\" + entry.getKey(), entry.getValue()));
            }
        }
        ResultVO resultVO = new ResultVO();
        ResponseData responseData = new SendCloudService().sendMail(vo);
        resultVO.setCode(responseData.getStatusCode());
        resultVO.setMsg(responseData.getMessage());
        return resultVO;
    }

}
