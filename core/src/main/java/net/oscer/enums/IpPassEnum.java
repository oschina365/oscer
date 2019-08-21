package net.oscer.enums;

import net.oscer.framework.LinkTool;

/**
 * @author kz
 * @create 2019-06-26 18:08
 **/
public class IpPassEnum {

    public static final String local = "127.0.0.1";
    public static final String local1 = "0:0:0:0:0:0:0:1";
    public static final String remote_local = LinkTool.getHost("local");
}
