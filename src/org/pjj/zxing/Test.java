package org.pjj.zxing;

public class Test {
    public static void main(String[] args) {
        String imgPath = "src/org/pjj/zxing/二维码.png";
//        String content = "HelloWorld你好";
        String content = "http://www.x4q.top/mts/xiaoku/";
        String imgType = "png";
        int widthAndHeight = 430;//正方形(二维码) 宽高都是一样的嘛
        String logo = "src/org/pjj/zxing/logo.png";//logo的路径

        //加密: 文字 -> 二维码
        ZXingUtil zXingUtil = new ZXingUtil();
        zXingUtil.encoderImt(imgPath,imgType,content,widthAndHeight,widthAndHeight,logo);

        //解密: 二维码 -> 文字
        String result = zXingUtil.decodeImg(imgPath);
        System.out.println(result);

    }
}
