package net.oscer.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.api.*;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.rabbitmq.tools.json.JSONUtil;
import net.oscer.framework.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 繁星数据库
 *
 * @author MRCHENIKE
 * @create 2021-09-05 8:44
 **/
public class DBFanXing {

    private final static Log log = LogFactory.getLog(DBFanXing.class);

    public static void main(String[] args) throws SQLException {
        //List<Map<String, Object>> maps = listPayOrders();
        /*for (Map<String, Object> map : maps) {
            Object o1 = map.get("transaction_id");
            Object o2 = map.get("pay_number");
            map.get("pay_status");
            tradeQuery(o1, o2, map.get("pay_status"));
        }*/
        //tradeQuery("2021090522001487275704343685",null,1);
        getToken();
        tradeCreate();
    }

    public static void getToken() {
        AlipayBaseConfig config = new AlipayBaseConfig();
        AlipaySystemOauthTokenRequest tokenRequest = new AlipaySystemOauthTokenRequest();
        tokenRequest.setGrantType("authorization_code");
        tokenRequest.setCode("258484c693e84bbdb9d04c7bd15bZC66");

        config.setAppId("2021002172654791");
        config.setPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCx4L89SahbFDEXTCGhIv22huwGcvL6UNglc1s9nmLPw/8jYLk0rEYO8gcw3P71ypbJUqP+EV2dAY4L6fDti6oGmO5HZiPXN6X2KFohOMdrUuoX+osJ8dXGAhTSBNlwFurL8mqa5fw8qbtFrz1cydULOZDBg3v8hwFUHwJeXRXJxT6VBa/Y/nH1iov1+6vLN1p40+wa3hq8Fk0Ay/bJCL/pAuBh0KdJFwerOnyLh9x4PR6BOIS64k/CA4p/WsLJNCPJ8PyfCf2t4M/Lg8yyNSy1F4F7j4tGiNXoD/z0UJkEfFSUjnxaaSzqv9ZrSK3DrrzIIOZskI9jEleeevnyUNK1AgMBAAECggEAbmjJa5pXxMjgu8xGp4VXpD9VK5+YECW0NHLI9JNmU/4dVPFJpFc2WTqDmiHio+Au/iGspxxSVg1MBTsdj+T8EYJFjM3qe0EQY52ibDKZHZXmtiGOwgp5HaHXGJFoAfpHnXYIE8OjcGgOVO+0D+87rO77WhJqGFIYUgW5a6ctygH6LHTC9BPuGuVywY/MvzsOjoNShE6gGGuByyTwd/+FHR6t5KTlKWVHBHVZoY0xwOZjUo7hPKvgqjgZYyG11xvQmeUBC5/0sybiqBrYZI4Fg9GwB/HYdEdgIbHR8qnQzf9HdtpuAgGwedAB+QdlCSlWo/rKmP3W9qpZeqbriTUfAQKBgQDjG20eI+6rWwvIttmWDglx/T6ijINlWzCwkqGu7d8wJem32fLyzcQ/unoms24RYzCongy+Yl2w7/Cr6eHfr1TVMj4U8EXOsFjTzGrk5A8hZOlPYJIprzUFOM3WGvI1LicU4aEuYlbgIwKBTv9vwgI881wtpRhFyfnfAp7mzrXXQQKBgQDIgf3RRPMQsBVkWHJz/dye3JMFh3mZYtzmY9CHpoBLGxjWWJynWJas61FnrftNCr1UKARcp53A0m8UW4r+JNsmJ4tdiYrOgLPRSOaE6wm32qkTwXn4kEYZikyE1AvSE3Bus6n37qvgIByP5q20vh0/w74hE8ptIEiYF/PGDzrydQKBgFtVCEj6wlz/PHn3rwF9m6bP5YSRZbY5OheIoKUs3HkMhjV93QpwXeATKlSuDDHJ5iOpjA50mKEznWJFqKTAailjDzx0dF5u5QooR0TNwpf3cSyO9bj10SjMc1sLcySX7vei1aNFwRJaggNmtatIJoZEiGmC34QRef+JmkXQE6kBAoGAP5Z6GFP1geVV42zpXz+zJ+5r8eT0+2APDkG5cEuthCQjzFZt1+SQZGZ8epUCjXxKbtYCDCBcNzqFX79oZRDrLz57RD7KdpL6c/TapmyjuFrwJAPH2rxmftMNUVKuLQFdNr5juO1INNv6MujFTfy86ev58COGnvUOEK4H8VgDKcECgYEArt96tbP5XPbyTQD1U+EY3kfPwpT+kI/l6ARcPgmf5mMbs+X1nSLK1dHOV69neGUSiUf3wRATzlgujqkSA0pjcux0aEGgN9pkNZ1ZkPAnQ2XYaCiwkj2Nu38PLApYklaV/f7/onPScJT3JgfkwHp2zWuqe4ObtUEQ5hhO+mj1IYk=");
        config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlDnhX++ERS8DicCxH2zFz8m67MjTs2nNlwVq11FIdCnBIcsucQ5f+HjfDSisu7C+ay6Kbkb+/NO0xADtimCrWQutFuUXrdPr+ClLTDtnHOqbFuVPkREjSn8hxGuVtGoa9OzDDBb6gg7EXah6+g0ICYOGp3JXph671b3/Oim9LUN/2nrahrUElw51m5LTUK9BBdK4SZJrIDqP6abd5O+OjmPGoRT6f8gktJTsrdcK9IRAwSpqOKOZjV7eAnTHvCyNn7HbTkd7QLnIMrwFH8Si3yvcm6OZiLyPPV8p6po0mv/icfGMl/f2Tw9bJPZNzly8rVWLPkQ9xgFy1ReR6uNHqQIDAQAB");
        config.setAppAuthToken("202109BB1e51a0b05d424bdeafec230555db5X22");

        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            tokenRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipaySystemOauthTokenResponse response = (AlipaySystemOauthTokenResponse) execute(tokenRequest, config);
        System.out.println(response);
    }

    public static void tradeCreate() {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeRequest tradeRequest = new AlipayTradeRequest();
        tradeRequest.setBuyerId("2088802557318870");
        tradeRequest.setTotalAmount(new BigDecimal(1));
        tradeRequest.setSubject("用户下单");
        tradeRequest.setOutTradeNo("20506117899340849152");
        request.setNotifyUrl("http://console.zwztf.net:9014/order/app/pay/10000001/alipay/callback");
        List<AlipayTradeGoodDetails> goodDetails = new ArrayList<AlipayTradeGoodDetails>() {{
            AlipayTradeGoodDetails details = new AlipayTradeGoodDetails();
            details.setGoodsName("iPhone12");
            details.setGoodsId("20210020345");
            details.setPrice(new BigDecimal(1));
            details.setQuantity(1);
            add(details);
        }};
        tradeRequest.setGoodDetails(goodDetails);

        AlipayBaseConfig aliconfig = aliconfig();
        request.setBizContent(JSONObject.toJSONString(tradeRequest));
        request.putOtherTextParam("app_auth_token", aliconfig.getAppAuthToken());
        AlipayTradeCreateResponse response = null;

        try {
            response = (AlipayTradeCreateResponse) getAlipayClient(aliconfig).execute(request);
        } catch (AlipayApiException var6) {
            var6.printStackTrace();
        }
        System.out.println(response);
    }


    /**
     * 执行
     *
     * @param request
     * @param config
     * @return
     */
    private static AlipayResponse execute(AlipayRequest request, AlipayBaseConfig config) {
        try {
            AlipayResponse response = getAlipayClient(config).execute(request);
            if (response.isSuccess()) {
                return response;
            }
        } catch (AlipayApiException e) {
        }
        return null;
    }

    private AlipayClient alipayClient;
    private static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";

    private static final String FORMAT_TYPE = "json";

    private static final String CHARSET = "utf-8";

    private static final String SIGN_TYPE_RSA2 = "RSA2";

    private static final String SIGN_TYPE_AES = "AES";

    private static AlipayClient getAlipayClient(AlipayBaseConfig config) {
        return new DefaultAlipayClient(ALIPAY_GATEWAY, config.getAppId(), config.getPrivateKey(),
                FORMAT_TYPE, CHARSET, config.getAlipayPublicKey(), SIGN_TYPE_RSA2);
    }

    public static void tradeQuery(Object trade_no, Object out_trade_no, Object pay_status) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject params = new JSONObject();
        if (trade_no != null) {
            params.put("trade_no", trade_no);
        }
        if (out_trade_no != null) {
            params.put("out_trade_no", out_trade_no);
        }
        request.setBizContent(params.toString());
        AlipayTradeQueryResponse response = null;
        AlipayBaseConfig conf = aliconfig();
        try {
            response = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", conf.getAppId(), conf.getPrivateKey(), "json", "utf-8", conf.getAlipayPublicKey(), "RSA2").execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (!response.isSuccess()) {
            log.error(String.format("alipay查询支付成功请求失败: {%s}", JSON.toJSONString(response)));
        }
        if (response.getTradeStatus().equals("TRADE_SUCCESS")) {
            if (!(pay_status.toString().equals("1"))) {
                log.warn(String.format("pay_number=%s的支付状态不对,阿里支付查询交易成功接口返回是支付的，但是表里的状态=%s", out_trade_no, pay_status));
            }
        }
        System.out.println(String.format("pay_number=%s的查询结果为：%s", out_trade_no, JSONObject.toJSON(response)));
    }

    public static AlipayBaseConfig aliconfig() {
        AlipayBaseConfig config = new AlipayBaseConfig();
        //config.setAppId("2021002169664144");
        //config.setPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDIqGEStNfogm1n+3DJyjeSpyofyhFQH1FHjPuRMZFAvXEPCbBpXQUQqVm2EXvt7E0Y5lVDgljpr2/GxigBUjj5SG7FM0GR0v0WlTmw8k9MQ6zPBXH4yT/+PsWsPWosM8VmH3FYjJ8Gfji62q0y5OYyyaQ4n/nelNPI76ru8zKotnKz+nMdQSmE8uHcV668HoPoU8pqj0DZfDk3IDJzi/mXSlct62vE+q6AHf/G7a+/e5D8gW2Pv7Nx92KCOZL5zKY7BZzxVT/ZRd0wZpTTdnHfafHeHfseEYBIdJR1zuJjx+DEXc0wSnSBdrLlgo+zx+QBLx4a7dDKKkQEP9eyMnatAgMBAAECggEAdJqEbp5wqUjB0x8ml9zPFoXV0MtX7DuGvBGNTv6C7iAoRxjWTyQjAV+oJklavxQGaWXQPn7MtF+ikNwt+zaQrSwOUsJKZDc7o5fsAouM0UCyd0suw+gPK+vGayT1QeKD36h98XsmHlvTE6Jn659gtjq/F7i/Nz09JRNBPcJNugPjESxWOtplgBk/sEe9pRhnr4SUIcVhQMaZhl59FoYAqIkGnG4sk4ixBCWMWC5ufdyccb9msVVzI8mmOuc1Ny1IIHYDv+5oOcMQHbjLDlyvG5ltdDhA7L3aByy/i5SajhmuBIVjTiUHv1vBuNoLHxdkHNnYQC/W4neUuQMqMnwOcQKBgQD8LY4S5r/TPfbUL2JVxIS/mskwO7eWHOmvqIx91i+hsWMZ2SalZ3sVut3YZuiSZMwEI8KKzUaI6duf6gYVz9rjmitdSaL1RHleVxs8z3FILySPVlepnyOSI3MndFb3APEJ1cLjR8ftjc9EDUKXkA+OK79nrbDpkfPfZVhHd2TJkwKBgQDLsu1H1LyKuAekv6Y69GMgEmm/YU0KERUJL/wyGAeSOlHvxsuLOii+uwr3V3Nx3G0RVaQhG5sYdGTvfmEJSafE9LOb+aLpDr2Ifw1jh4fh0LG8dZ0lLYVfESTD4OEXON2WcKOgZlfX4cAAVMveDPS7HnNqbYGUyuV+IiUyI0LmvwKBgQDDqRDf53uUijJWJcG1bDnXdGB/dQYXia1jJnKDPElOupR6vLq4gVbF4Gw6eHYhB+YnnBtYmLcOiexm4iwAhnp+N+NHiVfP12YX5ZvzjbyGVlWWhVaU/C2cOLcjOKg1E9zxVZLVx57+2RWk5mjxscvv8k+bQO/P+fdvMHHTWXaw0wKBgH/DgJS3WHLhrtmk44zC0JHDOtSxIPMZd8pFcLey3rzXG6F7XRu8pOXMx7oP/fcCvN5Mix/BAuy5xL161T5QFoywtx8z2vy3JCZDGpC853u5vGFqm+A/xxGz3Q2HNWetYT8E83yd9KcRj64lgaGLGs1q8hVlDgvXn04X+KCUAVhdAoGBALw5UmuL6zU0IAwTceP1x+eWFjIaa6qdOxIeZ985LDt8XN6oUWRREwr/x7+wNowuDdmJ8fKn2OA3Q6uWNwuICc8uTUJDgUPYV8kfRzMAmKRySxgJZKaBSusMpIsSv9ZKMx9yf+LZTlf7yvu1G2Jws5REAb4re0HW+fBX052anIFr");
        //config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj+N3vpKKEa6eqwi2a1x01UthiOtkcer1FocMxMFMOECLnZ/Hl4KC/kR8Ad5PKJvmLYa0fzJABBt5eQSKslNItQO1TgmLr9/SKuH0n4H8J3BMLTwfMvM1PtiyqivSF7zaNNBd3pFs9z6AXJaMyuwEu5zXNargOk5Z0GB4nadI+cV2keC7rIn9TA0w4q6EbU9ay/FVpTzT9x30OgIMMQPaC0nUhjTLzZ1okALdUAFCb3aZpXk7gbOmGY5J12thjCghpMTlIT5oOIlZeTHN8ujsBF3252utyxtq0Ptc+yEH6R1eM3p96vOpoejSYByAbs5OMAepE0p43W5zNSbjohsZBwIDAQAB");
        //config.setAppAuthToken("202108BB3354770dcc6e4c468f7500c686a32D68");
        config.setAppId("2021002172654791");
        config.setPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCx4L89SahbFDEXTCGhIv22huwGcvL6UNglc1s9nmLPw/8jYLk0rEYO8gcw3P71ypbJUqP+EV2dAY4L6fDti6oGmO5HZiPXN6X2KFohOMdrUuoX+osJ8dXGAhTSBNlwFurL8mqa5fw8qbtFrz1cydULOZDBg3v8hwFUHwJeXRXJxT6VBa/Y/nH1iov1+6vLN1p40+wa3hq8Fk0Ay/bJCL/pAuBh0KdJFwerOnyLh9x4PR6BOIS64k/CA4p/WsLJNCPJ8PyfCf2t4M/Lg8yyNSy1F4F7j4tGiNXoD/z0UJkEfFSUjnxaaSzqv9ZrSK3DrrzIIOZskI9jEleeevnyUNK1AgMBAAECggEAbmjJa5pXxMjgu8xGp4VXpD9VK5+YECW0NHLI9JNmU/4dVPFJpFc2WTqDmiHio+Au/iGspxxSVg1MBTsdj+T8EYJFjM3qe0EQY52ibDKZHZXmtiGOwgp5HaHXGJFoAfpHnXYIE8OjcGgOVO+0D+87rO77WhJqGFIYUgW5a6ctygH6LHTC9BPuGuVywY/MvzsOjoNShE6gGGuByyTwd/+FHR6t5KTlKWVHBHVZoY0xwOZjUo7hPKvgqjgZYyG11xvQmeUBC5/0sybiqBrYZI4Fg9GwB/HYdEdgIbHR8qnQzf9HdtpuAgGwedAB+QdlCSlWo/rKmP3W9qpZeqbriTUfAQKBgQDjG20eI+6rWwvIttmWDglx/T6ijINlWzCwkqGu7d8wJem32fLyzcQ/unoms24RYzCongy+Yl2w7/Cr6eHfr1TVMj4U8EXOsFjTzGrk5A8hZOlPYJIprzUFOM3WGvI1LicU4aEuYlbgIwKBTv9vwgI881wtpRhFyfnfAp7mzrXXQQKBgQDIgf3RRPMQsBVkWHJz/dye3JMFh3mZYtzmY9CHpoBLGxjWWJynWJas61FnrftNCr1UKARcp53A0m8UW4r+JNsmJ4tdiYrOgLPRSOaE6wm32qkTwXn4kEYZikyE1AvSE3Bus6n37qvgIByP5q20vh0/w74hE8ptIEiYF/PGDzrydQKBgFtVCEj6wlz/PHn3rwF9m6bP5YSRZbY5OheIoKUs3HkMhjV93QpwXeATKlSuDDHJ5iOpjA50mKEznWJFqKTAailjDzx0dF5u5QooR0TNwpf3cSyO9bj10SjMc1sLcySX7vei1aNFwRJaggNmtatIJoZEiGmC34QRef+JmkXQE6kBAoGAP5Z6GFP1geVV42zpXz+zJ+5r8eT0+2APDkG5cEuthCQjzFZt1+SQZGZ8epUCjXxKbtYCDCBcNzqFX79oZRDrLz57RD7KdpL6c/TapmyjuFrwJAPH2rxmftMNUVKuLQFdNr5juO1INNv6MujFTfy86ev58COGnvUOEK4H8VgDKcECgYEArt96tbP5XPbyTQD1U+EY3kfPwpT+kI/l6ARcPgmf5mMbs+X1nSLK1dHOV69neGUSiUf3wRATzlgujqkSA0pjcux0aEGgN9pkNZ1ZkPAnQ2XYaCiwkj2Nu38PLApYklaV/f7/onPScJT3JgfkwHp2zWuqe4ObtUEQ5hhO+mj1IYk=");
        config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlDnhX++ERS8DicCxH2zFz8m67MjTs2nNlwVq11FIdCnBIcsucQ5f+HjfDSisu7C+ay6Kbkb+/NO0xADtimCrWQutFuUXrdPr+ClLTDtnHOqbFuVPkREjSn8hxGuVtGoa9OzDDBb6gg7EXah6+g0ICYOGp3JXph671b3/Oim9LUN/2nrahrUElw51m5LTUK9BBdK4SZJrIDqP6abd5O+OjmPGoRT6f8gktJTsrdcK9IRAwSpqOKOZjV7eAnTHvCyNn7HbTkd7QLnIMrwFH8Si3yvcm6OZiLyPPV8p6po0mv/icfGMl/f2Tw9bJPZNzly8rVWLPkQ9xgFy1ReR6uNHqQIDAQAB");
        config.setAppAuthToken("202109BB1e51a0b05d424bdeafec230555db5X22");
        return config;
    }

    public static List<Map<String, Object>> listPayOrders() throws SQLException {
        String sql = "select * from s_pay_order";
        List<Map<String, Object>> result = DBDataFanXing.findBySql(sql);
        log.info(String.format("数量：%s", result.size()));
        return result;
    }

    public static class AlipayTradeRequest {
        @JSONField(name = "out_trade_no")
        private String outTradeNo;

        @JSONField(name = "total_amount")
        private BigDecimal totalAmount;

        private String subject;
        @JSONField(name = "buyer_id")
        private String buyerId;

        private String notifyUrl;

        @JSONField(name = "goods_detail")
        private List<AlipayTradeGoodDetails> goodDetails;

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public List<AlipayTradeGoodDetails> getGoodDetails() {
            return goodDetails;
        }

        public void setGoodDetails(List<AlipayTradeGoodDetails> goodDetails) {
            this.goodDetails = goodDetails;
        }
    }

    public static class AlipayTradeGoodDetails {
        @JSONField(name = "goods_id")
        private String goodsId;
        @JSONField(name = "goods_name")
        private String goodsName;
        private Number quantity;
        private BigDecimal price;
        @JSONField(name = "goods_category")
        private String goodsCategory;
        @JSONField(name = "categories_tree")
        private String categoriesTree;
        private String body;
        @JSONField(name = "show_url")
        private String showUrl;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public Number getQuantity() {
            return quantity;
        }

        public void setQuantity(Number quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getGoodsCategory() {
            return goodsCategory;
        }

        public void setGoodsCategory(String goodsCategory) {
            this.goodsCategory = goodsCategory;
        }

        public String getCategoriesTree() {
            return categoriesTree;
        }

        public void setCategoriesTree(String categoriesTree) {
            this.categoriesTree = categoriesTree;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getShowUrl() {
            return showUrl;
        }

        public void setShowUrl(String showUrl) {
            this.showUrl = showUrl;
        }
    }
}
