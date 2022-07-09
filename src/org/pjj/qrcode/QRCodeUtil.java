package org.pjj.qrcode;

import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class QRCodeUtil {

    /**
     * 加密: 文字信息 -> 二维码
     * @param content 要加密的文字信息
     * @param imgPath 生成二维码后的存放路径
     * @param imgType 生成二维码的后缀(格式) 如: jpg,png
     * @param size 生成的二维码的尺寸  二维码是正方形 所以size是边长
     * @return 是否成功生成二维码 成功生成为true 否则为false
     */
    public boolean encoderQRCode(String content,String imgPath,String imgType,int size){

        boolean result = false;

        //内存中的一张图片
        RenderedImage bufImg = qRcodeCommon(content,imgType,size);

        //最后生成的二维码 就写到这个文件中
        File file = new File(imgPath);

        try {
            //生成图片      生成成功返回true  反之 false
            result = ImageIO.write(bufImg, imgType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过此方法 生成一个 RenderedImage对象   RenderedImage对象 代表 内存中的一张图片     最后将这个照片写入文件
     * @param content 要写入到二维码的文字信息
     * @param imgType 图片格式
     * @param size 二维码边长
     * @return RenderedImage对象
     */
    public RenderedImage qRcodeCommon(String content,String imgType,int size){
        BufferedImage bufImg = null;

        //QRCode 是怎么在二维码中写入的  大概是这么回事  比如需要将HelloWorld写入 二维码 需要先将HelloWorld 转成 boolean值的二维数组
        //然后 二维码上面的黑色小方块 不就像是一个二维数组嘛 然后数组元素为true就画一个黑色小方块 为 false就啥都不干
        //最后 就生成了 很多不规律的 黑色小方块 就组成了一个二维码.

        //String -> boolean[][]     将字符串转为boolean的二维数组 需要借助于 Qrcode类    肯定不能瞎转啊,就相当于解码和编码  肯定要是一样的技术 要不然就会有乱码
        Qrcode qrcodeHandler = new Qrcode();
        //设置二维码的排错率: (7%)L<M<Q<H(30%): 排错率越高,可存储的信息越少,但是对二维码清晰度要求越小.
        qrcodeHandler.setQrcodeErrorCorrect('M');
        //可存放的信息类型: N:数字、A:数字+A~Z、B:所有
        qrcodeHandler.setQrcodeEncodeMode('B');
        //尺寸: 取值范围: 1~40(这是老师说的)    但是 我发现好像是可以超出40的,而且超出了40还是会有效果
        qrcodeHandler.setQrcodeVersion(size);

        //这就是将 字符串 转为 boolean类型的二维数组的方法, 这个方法需要一个byte[]数组  我们可以通过String提供的getBytes()将字符串转成byte[]
        boolean[][] calQrcode = qrcodeHandler.calQrcode(content.getBytes(Charset.forName("utf-8")));

        //有一个问题: 就是上面说qrcodeHandler.setQrcodeVersion(size);设置尺寸 取值范围1~40, 但是画二维码就算是40*40的也是太小了,
        //所以虽然输入的取值范围是1~40但是我们会通过一些公式将这个尺寸 变大 然后在设置成二维码的长宽.
        //我觉得这个1~40设置的是二维码占二维码这种图片的比例吧,又不是一个二维码图片 全是黑色小方块(个人感觉,上面是老师说的)
        int imgSize = 67 + 12*(size - 1);

        //RenderedImage对象 代表 内存中的一张图片   我们需要操作这个对象来生成图片
        bufImg = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);//RGB(Red Green Blue)红 绿 蓝 三原色

        Graphics2D graphics = bufImg.createGraphics();//创建一个 画板 (二维画板 平面的)
        graphics.setBackground(Color.white);//将画板的背景色 设置为白色white

        //初始化 白色在画板中 占多高多宽  0,0通过x轴y轴定义出一个点  size,size再定义处一个点 然后这两个点中间的内容就都是白色
        //由于在创建 RenderedImage对象时就设置了这个图片的长宽为size 现在设置背景色 x坐标y坐标为0,0 和 size,size 就相当于这整个图片背景色都是白色
        graphics.clearRect(0,0,imgSize,imgSize);
        graphics.setColor(Color.black);//设置 画板上 图像的颜色 (设置二维码的颜色)
        int pixoff = 2;//偏移量  就是说画黑色小方块的画 不能贴着二维码图片的边边画把, 你见过哪个二维码 那黑色小方块帖这边了的. 所以画黑色小方块的时候需要偏移一点

        //遍历 需要画的二维数组
        for(int i=0;i<calQrcode.length;i++){
            for(int j=0;j<calQrcode[i].length;j++){
                if(calQrcode[i][j] == true){//数组元素为true就画一个黑色小方块 为 false就啥都不干
                    //四个参数依次为 x轴坐标  y轴坐标  宽  高
                    //i*3 j*3 是因为一个黑色小方块宽高为3 现在i和j每次循环是+1
                    //那么第一次画是0,0,第二次画是0,1 那不是两个黑色小方块重叠了嘛,因为黑色小方块宽高是3
                    //i*3+pixoff 完事了, 为啥还要加个pixoff呢, 因为你见过哪个二维码 那黑色小方块帖这边了的. 所以画黑色小方块的时候需要偏移一点
                    //所以 第一次画是0,0 *3后 变成 0,0 +偏移量 2,2   第二次画 0,1 *3后变成 0,3 +偏移量 2,5 以此类推
                    graphics.fillRect(i*4+pixoff,j*4+pixoff,4,4);//画黑色小方块 通过x轴 y轴 定位出在哪画黑色小方块
                }
            }
        }
        //增加LOGO
        Image logo = null;
        try {
            //将硬盘中的logo.png这张图片加载到 Image logo 这个对象中(内存中)  作为我们二维码的logo
            logo = ImageIO.read(new File("src/org/pjj/qrcode/logo.png"));
            int maxHeight = bufImg.getHeight();//获取原二维码的高度
            int maxWidth = bufImg.getWidth();//获取原二维码的宽度

            //在以生成的二维码上画logo
            //五个参数依次为:
            //logo 需要画的图片 因为logo是一个Image对象 一个Image代表一个图片,由于使用的是画二维码的画笔,所以要将 需要画的图片logo传过去
            //imgSize*(2/5) 与 imgSize*(2/5)分别是x坐标与y坐标  定义处一个点,这个点是logo的左上角 然后开始画logo(就是说在哪里画logo)
            //imgSize*(2/5) 大概就是 原二维码的 x轴 五分之二的地方 同样 y轴 也是五分之二的地方   大概就在二维码中间了.
            //maxHeight/5 与 maxWidth/5 需要把logo画多大,这里是原二维码长宽的 五分之一 就是说画出来的logo是的边长是 原二维码边长的五分之一.
            //最后一个参数 是 需要一个ImageObserver类型的对象 至于这里为什么是null,我也不是很清楚.
            graphics.drawImage(logo,42,42,maxWidth/5,maxHeight/5,null);
//            int x = (int)Math.floor(imgSize*(2/5));
//            System.out.println(x); 0 不晓得为什么算不出来


        } catch (IOException e) {
            e.printStackTrace();
        }


        graphics.dispose();//释放画板使用的内存     和IO里面的input.close();一样
        bufImg.flush();//清理 将流里面的数据强行刷到目的地     和IO里面的flush()一样

        return bufImg;
    }



    //解密: 二维码(图片路径) -> 文字信息
    public String decoderQRCode(String imgPath){
        String result = "";
        try {
            //先将 需要解密的二维码(图片) 根据 图片路径 加载到内存
            BufferedImage bufImg = ImageIO.read(new File(imgPath));

            //解密
            QRCodeDecoder decoder = new QRCodeDecoder();
            TwoDimensionCodeImage tdcImage = new TwoDimensionCodeImage(bufImg);
            //解密 需要一个 QRCodeImage类型的对象 我们整了个TwoDimensionCodeImage实现了QRCodeImage接口
            //QRCodeImage接口 里的方法 大概就是 返回二维码的一些信息 我们通过构造方法将一个 二维码传递到了TwoDimensionCodeImage类中
            byte[] decode = decoder.decode(tdcImage);//将二维码解密后: 返回解密后的文字信息(不过是以字节数组的形式)
            result = new String(decode,"utf-8");//我们将字节数组 转成字符串
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
