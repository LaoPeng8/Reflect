package org.pjj.zxing;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ZXingUtil {

    /**
     * 加密: 文字 -> 二维码
     * @param imgPath 生成的图片 在哪里?    生成的二维码的路径
     * @param format 生成的二维码的格式  如: png gif jpg
     * @param content 需要写入二维码的文本信息
     * @param width 图片(二维码)的宽
     * @param height  图片(二维码)的高
     * @param logo 需要加在二维码中的logo 的路径
     */
    public void encoderImt(String imgPath,String format,String content,int width,int height,String logo){

        //hints: 加密涉及的一些参数: 编码、排错率
        Hashtable<EncodeHintType,Object> hints = new Hashtable<>();
        //排错率 L<M<Q<H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //编码
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
        //外边距
        hints.put(EncodeHintType.MARGIN,1);

        BitMatrix bitMatrix = null;
        try {
            /**
             * 四个参数分别为: content需要加密的文字(写入到二维码的文字)
             * BarcodeFormat.QR_CODE 这个ZXing可以处理很多码 如条形码,二维码  这个参数就代表是什么码
             * width 和 height  就是宽和高
             * hints: 加密涉及的一些参数: 编码、排错率
             */
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        /**
         * 这个对象代表内存中的一张图片(此处是二维码)  -> 需要一个boolean[][]  ->  需要一个BitMatrix对象
         * 就是说 二维码需要一个 boolean[][] 才能画出来  而 boolean[][] 需要一个BitMatrix对象才能产生
         * 参数为: 这个图片的宽高,和这个图片可以由什么颜色组成(RGB三原色)
         */
        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                img.setRGB(x,y, (bitMatrix.get(x,y)?Color.black.getRGB():Color.white.getRGB()));
            }
        }
        /**
         * 上面就是怎么画的二维码: 依次给每个坐标的点 赋个颜色 如果bitMatrix.get(x,y)这个坐标是true,则赋个黑色,若为false则赋个白色
         * 最后就组成了一个二维码.
         */

        //画logo     传入个一个二维码  和  一个logo的路径
        img = LogoUtil.logoMatrix(img, logo);


        File file = new File(imgPath);
        try {
            //生成图片 通过ImageIO.write可以将一个 内存中的图片 写到 一个文件中
            ImageIO.write(img,format,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //解密: 二维码 -> 文字
    public String decodeImg(String imgPath){
        File file = new File(imgPath);
        if(!file.exists()){
            return "";
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultiFormatReader formatReader = new MultiFormatReader();

        LuminanceSource source = new BufferedImageLuminanceSource(img);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        // 将一个 图片 解析成 Result
        Result result = null;
        try {
            Map<DecodeHintType,Object> map = new HashMap<>();
            map.put(DecodeHintType.CHARACTER_SET,"utf-8");
            result = formatReader.decode(binaryBitmap,map);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }





}
