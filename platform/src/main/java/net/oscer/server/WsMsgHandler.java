package net.oscer.server;

import net.oscer.beans.Tweet;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.controller.BaseController;
import net.oscer.dao.TweetDAO;
import net.oscer.db.Entity;
import net.oscer.framework.StringUtils;
import net.oscer.view.TweetViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanyaowu
 * 2017年6月28日 下午5:32:38
 */
public class WsMsgHandler implements IWsMsgHandler {
    private static Logger log = LoggerFactory.getLogger(WsMsgHandler.class);

    public static final WsMsgHandler me = new WsMsgHandler();

    private WsMsgHandler() {

    }

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     */
    @Override
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientip = request.getClientIp();
        String myname = request.getParam("t");
        Tio.bindUser(channelContext, myname);
        channelContext.setUserid(myname);
        return httpResponse;
    }

    /**
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     * @author tanyaowu
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //绑定到群组，后面会有群发
        Tio.bindGroup(channelContext, Const.GROUP_ID);
        //int count = Tio.getAllChannelContexts(channelContext.groupContext).getObj().size();

        //String msg = "{name:'admin',message:'" + channelContext.userid + " 进来了，共【" + count + "】人在线" + "'}";
        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        //WsResponse wsResponse = WsResponse.fromText(msg, ServerConfig.CHARSET);
        //群发
        //Tio.sendToGroup(channelContext.groupContext, Const.GROUP_ID, wsResponse);
    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext, "receive close flag");
        return null;
    }

    /*
     * 字符消息（binaryType = blob）过来后会走这个方法
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        HttpRequest httpRequest = wsSessionContext.getHandshakeRequestPacket();//获取websocket握手包
        if (log.isDebugEnabled()) {
            log.debug("握手包:{}", httpRequest);
        }

        String[] cs = text.split("%&%");
        Map<String, String> map = new HashMap<>();
        if (cs.length > 1) {
            for (int i = 0; i < cs.length; i++) {
                String[] as = cs[i].split("%@%");
                map.put(as[0], as[1]);
            }
        } else {
            return null;
        }

        String uid = map.get("uid");
        if (uid == null || StringUtils.isBlank(uid)) {
            return null;
        }
        User user = BaseController.getUserByCookie(URLEncoder.encode(uid));
        if (user == null || user.getId() <= 0 || user.getStatus() != User.STATUS_NORMAL || user.getOnline() != Entity.ONLINE) {
            return null;
        }

        if (StringUtils.isBlank(text)) {
            return null;
        }
        System.out.println(String.format("登录用户%s,动弹发布的内容：%s", user.getName(), text));
        Tweet tweet = TweetDAO.ME.push(httpRequest, user.getId(), map);

        // String msg = "动弹发布成功";
        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        WsResponse wsResponse = WsResponse.fromText(ApiResult.successWithObject(new TweetViewObject(tweet.getId())).json(), ServerConfig.CHARSET);
        //群发
        Tio.sendToGroup(channelContext.groupContext, Const.GROUP_ID, wsResponse);

        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }

}
