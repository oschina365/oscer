package net.oscer.dao;


import net.oscer.beans.SendEmailTemplate;
import net.oscer.framework.StringUtils;

public class SendEmailTemplateDAO extends CommonDao<SendEmailTemplate> {

    public static final SendEmailTemplateDAO ME = new SendEmailTemplateDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 通过类型查询对应的邮件模板
     *
     * @param type
     * @return
     */
    public SendEmailTemplate selectByType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        String sql = "select * from send_email_templates where type=? limit 1";
        return getDbQuery().read(SendEmailTemplate.class, sql, type);
    }
}
