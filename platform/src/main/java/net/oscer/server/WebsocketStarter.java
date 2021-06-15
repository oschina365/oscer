package net.oscer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

/**
 * @author tanyaowu
 * 2017年6月28日 下午5:34:04
 */
public class WebsocketStarter {

    private final static Logger log = LoggerFactory.getLogger(WebsocketStarter.class);


    private WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;

    /**
     * @author tanyaowu
     */
    public WebsocketStarter(int port, WsMsgHandler wsMsgHandler) throws Exception {
        wsServerStarter = new WsServerStarter(port, wsMsgHandler);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(ServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(ServerAioListener.me);

        //设置ip监控
        serverGroupContext.setIpStatListener(StatListener.me);
        //设置ip统计时间段
        serverGroupContext.ipStats.addDurations(ServerConfig.IpStatDuration.IPSTAT_DURATIONS);

        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(ServerConfig.HEARTBEAT_TIMEOUT);

    }

    /**
     * @throws IOException
     * @author tanyaowu
     */
    public static void start() throws Exception {
        WebsocketStarter appStarter = new WebsocketStarter(ServerConfig.SERVER_PORT, WsMsgHandler.me);
        appStarter.wsServerStarter.start();
    }

    /**
     * @return the serverGroupContext
     */
    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    public static void main(String[] args) throws Exception {
        //启动http server，这个步骤不是必须的，但是为了用页面演示websocket，所以先启动http
        //P.use("app.properties");


        /*if (P.getInt("start.http", 1) == 1) {
            HttpServerInit.init();
        }*/

        //启动websocket server
        start();
    }

}
