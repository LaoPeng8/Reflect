package org.pjj.qrcode;

public class Test {
    public static void main(String[] args) {
        //生成二维码
        /**
         * 生成图片的路径      src/org/pjj/qrcode/二维码.png
         * 文字信息 或 图片信息 或 网址信息 (就是说 扫二维码 可以扫个什么东西出来): "HelloWorld"
         * 图片类型(后缀) imgType = "png";
         * size 图片尺寸
         */
        String imgPath = "src/org/pjj/qrcode/二维码.png";
        String content = "HelloWorld张三";
        String imgType = "png";
        int size = 105;

        /**
         * 生成二维码
         * 加密: 文字信息 -> 二维码
         */
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
//        qrCodeUtil.encoderQRCode(content,imgPath,imgType,size);

        //画个百度网址的二维码.(自己搞滴玩滴)
//        qrCodeUtil.encoderQRCode("http://www.baidu.com","src/org/pjj/qrcode/baidu.png","png",105);

        /**
         * 解密: 二维码 -> 文字信息
         */
        String contentResult = qrCodeUtil.decoderQRCode(imgPath);//返回将二维码 解码后的文字信息
        System.out.println(contentResult);

    }
}
