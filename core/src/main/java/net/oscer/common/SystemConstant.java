package net.oscer.common;


import net.oscer.framework.ConfigTool;

/**
 * @author kz
 * @date 2017年12月1日14:32:07
 * @desc 七牛参数
 */
public class SystemConstant {

    public final static String SEPARATOR_UNDERLINE = "_";

    /**
     * 点
     */
    public final static String SEPARATOR_DOT= ".";


    public static final String QINIU_ACCESS = ConfigTool.getProp("qiniu.access");
    public static final String QINIU_SECRET = ConfigTool.getProp("qiniu.secret");
    public static final String QINIU_BUCKET = ConfigTool.getProp("qiniu.bucket");
    /**
     * 接受上传图片的类型
     */
    public static final String QINIU_SUFFIXIMAGE = ConfigTool.getProp("qiniu.suffixImage");
    /**
     * 重命名图片名称连接符号
     */
    public static final String QINIU_FILELINK = ConfigTool.getProp("qiniu.filelink");

    /**
     * 斜线
     */
    public static final String QINIU_SLASH = ConfigTool.getProp("qiniu.fileslash");
}
