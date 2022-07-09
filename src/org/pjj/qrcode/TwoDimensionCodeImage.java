package org.pjj.qrcode;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

public class TwoDimensionCodeImage implements QRCodeImage {

    BufferedImage bufImg;//内存中的二维码(图片)

    public TwoDimensionCodeImage(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getWidth() {//二维码的宽
        return bufImg.getWidth();
    }

    @Override
    public int getHeight() {//二维码的高
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {//二维码的 像素点?
        return bufImg.getRGB(x,y);
    }
}
