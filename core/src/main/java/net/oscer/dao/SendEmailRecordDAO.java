package net.oscer.dao;


import net.oscer.beans.SendEmailRecord;

public class SendEmailRecordDAO extends CommonDao<SendEmailRecord> {

    public static final SendEmailRecordDAO ME = new SendEmailRecordDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 查询邮件记录
     *
     * @param email
     * @param type
     * @param email_type
     * @return
     */
    public SendEmailRecord selectByEmail(String email, String type, String email_type) {
        String sql = "select * from send_email_records where receive_email=? and type=? and email_type=? order by last_date desc limit 1";
        return getDbQuery().read(SendEmailRecord.class, sql, email, type, email_type);
    }
}
