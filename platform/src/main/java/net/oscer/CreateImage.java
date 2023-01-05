package net.oscer;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 生成健康证图片
 *
 * @类 名 : CreateImage
 * @功能描述 : TODO
 * @作者信息 : 崔胖子
 * @创建时间 : 2017-9-10上午12:21:09
 * @修改备注 :
 */
public class CreateImage {

    /**
     * 模板图片路径
     */
    private static String TEMPLATEPAHT = "";

    /**
     * 模板印章路径
     */
    private static String TEMPLATEPATHYZ = "";

    static {
        // 初始化健康证图片模板路径
        TEMPLATEPAHT = "D:\\works\\mine\\oscer\\docs\\jiankangzheng.png";
        // 初始化健康证图片模板印章路径
        TEMPLATEPATHYZ = "D:\\健康证章.png";
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("age", 29);
        map.put("no", "221008276");
        map.put("name", "柯真");
        map.put("gender", "男");
        map.put("begin", "2022年10月08日");
        map.put("project", "外卖配送");
        map.put("idCode", "421127199305132852");
        map.put("time", "有效期：2022年10月08日至2023年10月8日");
        map.put("facePhoto", "D:\\works\\mine\\oscer\\docs\\11.jpg");
        createImage(map, "D:\\works\\mine\\oscer\\docs\\11-1.jpg");
        System.out.println("生成完毕");
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;

        }//This code ensures that all the pixels in the image are loaded

        image = new ImageIcon(image).getImage();

        BufferedImage bimage = null;

        GraphicsEnvironment ge = GraphicsEnvironment

                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;

            GraphicsDevice gs = ge.getDefaultScreenDevice();

            GraphicsConfiguration gc = gs.getDefaultConfiguration();

            bimage = gc.createCompatibleImage(image.getWidth(null),

                    image.getHeight(null), transparency);

        } catch (HeadlessException e) {//The system does not have a screen

        }
        if (bimage == null) {//Create a buffered image using the default color model

            int type = BufferedImage.TYPE_INT_RGB;

            bimage = new BufferedImage(image.getWidth(null),

                    image.getHeight(null), type);

        }//Copy image to buffered image

        Graphics g = bimage.createGraphics();//Paint the image onto the buffered image

        g.drawImage(image, 0, 0, null);

        g.dispose();
        return bimage;
    }

    /**
     * @throws :
     * @Title : createImage
     * @功能描述 : TODO
     * @设定文件 : @param map 需要填充的数据集合
     * @设定文件 : @param path 文件输出的路径+文件名称
     * @设定文件 : @return
     * @返回类型 : String 文件路径
     */
    public static void createImage(Map<String, Object> map, String path) {
        try {
            // 加载模板图片
            //BufferedImage image = ImageIO.read(new File(TEMPLATEPAHT));
            Image src = Toolkit.getDefaultToolkit().getImage(TEMPLATEPAHT);
            BufferedImage image = toBufferedImage(src);//Image to BufferedImage;
            // 得到图片操作对象
            Graphics2D graphics = image.createGraphics();
            //消除文字锯齿
            //graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //消除图片锯齿
            //graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 设置文字的颜色为黑色
            graphics.setColor(Color.black);
            // 设置文字的字体,大小
            graphics.setFont(new Font("宋体", Font.BOLD, 60));
            // 健康证印章
            //File files = new File(TEMPLATEPATHYZ);
            // 年龄
            graphics.drawString(map.get("age") == null ? "" : map.get("age").toString(), 1620, 580);
            // 编号
            String no = map.get("no") == null ? "" : map.get("no").toString();
            graphics.drawString("编号:" + no, 135, 1130);
            // 姓名
            graphics.drawString(map.get("name") == null ? "" : map.get("name").toString(), 1170, 400);
            // 性别
            graphics.drawString(map.get("gender") == null ? "" : map.get("gender").toString(), 1170, 580);
            // 有效期开始时间-发证日期
            graphics.drawString(map.get("begin") == null ? "" : map.get("begin").toString(), 1170, 930);
            // 经营项目
            graphics.drawString(map.get("project") == null ? "" : map.get("project").toString(), 1170, 750);
            // 身份证号
            graphics.drawString(map.get("idCode") == null ? "" : map.get("idCode").toString(), 1170, 1110);
            // 有效期：
            graphics.drawString(map.get("time") == null ? "" : map.get("time").toString(), 130, 1210);
            // 创建头像地址
            String paths = map.get("facePhoto") == null ? "D:\\健康证默认头像.png" : map.get("facePhoto").toString();
            File file = new File(paths);
            // 对头像进行裁剪
            Image img = thumbnail(file, 500, 690);
            // 将头像放入模板中
            graphics.drawImage(img, 130, 130, null);
            // 对印章进行裁剪
            //Image imgs = thumbnail(files, 895, 639);
            // 将印章放入模板中
            //graphics.drawImage(imgs, 440, 440, null);
            // 将健康证图片存储到本地
            createImage(path, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将image对象存储到本地
     *
     * @throws :
     * @Title : createImage
     * @功能描述 : TODO
     * @设定文件 : @param fileLocation 本地路径
     * @设定文件 : @param image 图片对象
     * @返回类型 : void
     */
    private static void createImage(String fileLocation, BufferedImage image) {
        try {
            String formatName = fileLocation.substring(fileLocation.lastIndexOf(".") + 1);
            ImageIO.write(image, formatName, new File(fileLocation));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片压缩-按照固定宽高原图压缩
     *
     * @throws :
     * @Title : thumbnail
     * @功能描述 : TODO
     * @设定文件 : @param img 本地图片地址
     * @设定文件 : @param width 图片宽度
     * @设定文件 : @param height 图片高度
     * @设定文件 : @return
     * @设定文件 : @throws IOException
     * @返回类型 : Image
     */
    public static Image thumbnail(File img, int width, int height) throws IOException {
        BufferedImage BI = ImageIO.read(img);
        Image image = BI.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = tag.getGraphics();
        g.setColor(Color.WHITE);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return image;
    }

}