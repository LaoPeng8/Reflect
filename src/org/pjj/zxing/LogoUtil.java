package org.pjj.zxing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LogoUtil {

    /**
     * 传入一个logo 传入一个二维码  返回一个带logo的二维码
     * @param matrixImage 二维码
     * @param logo logo
     * @return 返回的带logo的二维码
     */
    public static BufferedImage logoMatrix (BufferedImage matrixImage,String logo){

        //将logo  String类型的logo的路径 变为 BufferedImage内存中的一个logo
        BufferedImage logoImg = null;
        try {
            logoImg = ImageIO.read(new File(logo));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //在二维码上画logo        在二维码上创建画板
        Graphics2D graphics = matrixImage.createGraphics();

        //二维码的宽和高
        int width = matrixImage.getWidth();
        int height = matrixImage.getHeight();

        /**
         * 需要在二维码上画什么   换一个 logoImg(纯logo)
         * 在二维码的什么地方画   x,y坐标 五分之二的地方画
         * 画的logo有多大        在原二维大小的 五分之一
         * 最后一个参数 是 需要一个ImageObserver类型的对象 至于这里为什么是null,我也不是很清楚.
         */  //这是画的一个纯logo
        graphics.drawImage(logoImg,width*2/5,height*2/5,width*1/5,width*1/5,null);

        /**
         * 产生一个画笔 白色圆角正方形的画笔      在logo外层画一圈白色正方形包围住logo
         * 第一个参数为: 画笔的 粗细  就是说画笔画出来的线条的粗细       5(粗细为5)
         * 第二个参数为: 画笔的笔头  是尖的还是圆的                   BasicStroke.CAP_ROUND(是圆的笔头)
         * 第三个参数为: 画两条线的交界处 是尖和还是圆的  也就是说 那种有角度的 角 是尖角 还是 圆角 BasicStroke.JOIN_ROUND(是圆角)
         */
        BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        //将 画笔 和 画板 关联起来        比如说有十只画笔  也不知道是哪只笔在画板上画吧  所以需要关联起来
        graphics.setStroke(stroke);

        /**
         * 产生 一个画 长方形的对象     正方形也是长方形的一种嘛
         * 参数依次为:
         * 在二维码的什么地方画   x,y坐标 五分之二的地方画
         * 画的长方形有多大        在原二维大小的 五分之一
         * 画笔的笔头  是尖的还是圆的  BasicStroke.CAP_ROUND(是圆的笔头)
         * 画两条线的交界处 是尖和还是圆的  也就是说 那种有角度的 角 是尖角 还是 圆角  BasicStroke.JOIN_ROUND(是圆角)
         */
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(width*2/5,height*2/5,width*1/5,height*1/5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        graphics.setColor(Color.white);//画的正方形是白色的
        graphics.draw(round);//画正方形     将我们之前准备的好的 长方形对象(正方形) 当做参数放到 画笔的这个方法里  画就完事

        /**
         * 产生一个画笔 灰圆角正方形的画笔      在logo外层画一层灰色的圆角正方形
         * 第一个参数为: 画笔的 粗细  就是说画笔画出来的线条的粗细       1(粗细为1)
         * 第二个参数为: 画笔的笔头  是尖的还是圆的                   BasicStroke.CAP_ROUND(是圆的笔头)
         * 第三个参数为: 画两条线的交界处 是尖和还是圆的  也就是说 那种有角度的 角 是尖角 还是 圆角 BasicStroke.JOIN_ROUND(是圆角)
         */
        //产生一个画笔 灰圆角正方形的画笔      在logo外层画一层灰色的圆角正方形      同上面 产生画白色圆角正方形的画笔一样
        BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        graphics.setStroke(stroke2);//将 画笔 和 画板 关联起来(之前关联过一个画笔 但是现在不用了, 现在使用这个画笔,所以就关联这个画笔)

        //画一个灰色圆角正方形    同上面 画白色圆角正方形一样
        //灰色圆角正方形 在 白色圆角正方形的里面 所以坐标+了个4
        //由于灰色圆角正方形 在 白色圆角正方形的里面 就是 灰色正方形 别 白色正方形小 所以宽高-了8
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(width*2/5+4,height*2/5+4,width*1/5-8,height*1/5-8,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        graphics.setColor(Color.GRAY);//画的正方形是灰色的   如果没有灰色可以通过RGB调一个  new Color(128,128,128) 这就是代表灰色
        graphics.draw(round2);//画正方形     将我们之前准备的好的 长方形对象(正方形) 当做参数放到 画笔的这个方法里  画就完事

        graphics.dispose();//释放内存

        //刷新一下 因为之前往这个图片中画了很多东西
        matrixImage.flush();//之前是个这个二维码(图片) 中画的各种东西 如: logo 白色正方形 灰色正方形

        return matrixImage;//将画了很多东西的二维码返回
    }

}
