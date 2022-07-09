package org.pjj.javamail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * 测试发送复杂邮件 (简单邮件就是 只有收件人,发件人,标题,正文) (复杂文件就是 在简单邮件的基础上 加上了附件,图片等)
 * @author PengJiaJun
 */
public class JavaMailWithAttachmentDemo {
    public static void main(String[] args) throws Exception {

        //配置一些 邮件服务器的信息
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol","smtp");//使用协议: smtp   邮箱协议有很多 如POP3、SMTP、IMAP
        props.setProperty("mail.smtp.host","smtp.qq.com");//使用qq邮箱smtp协议的地址  百度一搜就可以搜出 什么邮箱的什么协议地址
        props.setProperty("mail.smtp.port","465");//qq邮箱smtp协议 的端口
        props.setProperty("mail.smtp.auth","true");//邮箱是否需要授权(一般都需要授权)   此处设置为true 需要授权
        //如果是163邮箱 到这里就配置完了   如果是QQ邮箱(或者说其他邮箱) 需要一个 SSL安全认证
        props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");//java提供一个类来处理SSL认证javax.net.ssl.SSLSocketFactory
        props.setProperty("mail.smtp.socketFactory.fallback","false");//为false 表示只处理 经过SSL认证的邮件     为true表示都处理
        props.setProperty("mail.smtp.socketFactory.port","465");//SSL认证也需要端口号   一般与smtp是同一个端口号


        //Session没有供外界使用的构造方法 但是对外界提供了一个静态的 返回一个session的方法(需要传入一个含有一些配置信息的properties)
        Session session = Session.getInstance(props);//java 就是通过 session 和 邮件服务器 交互
        session.setDebug(true);//开启日志提示

        //创建一封邮件 (内部包含了 发件人 收件人 发送的邮件的内容)
        MimeMessage message = createMimeMessage(session, "2779824672@qq.com","3119118981@qq.com","抄送人地址xxx","密送人地址xxx");

        //获取传输对象  通过这个对象来发送
        Transport transport = session.getTransport();

        //建立连接      通过qq与密码(因为是qq的邮件服务器) 其中密码以授权码的形式体现(毕竟密码这东西不方便明着写)
        //授权码 可以在qq邮箱的 设置中 查看到(需要设置开启smtp服务器)
        transport.connect("2779824672@qq.com","swybqpgevizvdfha");

        transport.sendMessage(message,message.getAllRecipients());//需要发送的邮件 与 收件人地址(收件人地址之前在message对象中设置过 所以这里直接获取就行了)

        transport.close();
    }

    //创建 带图片+附件的邮件
    public static MimeMessage createMimeMessage(Session session,String sendAddress,String receiveAddress,String cReceiveAddress,String mReceiveAddress) throws MessagingException, UnsupportedEncodingException {
        /**
         * 一个MimeMessage对象就是一封邮件
         * 简单邮件: 标题、正文、收件人、发件人
         * 复杂一点的邮件 还有 {附件、图片}
         */
        MimeMessage message = new MimeMessage(session);//需要传入一个session

        Address address = new InternetAddress(sendAddress,"发件人姓名_小明","utf-8");//发件人地址 发件人名字 字符编码
        message.setFrom(address);//设置发件人

        //设置发件人
        //收件人类型: 普通收件人(MimeMessage.RecipientType.TO)、抄送(MimeMessage.RecipientType.CC)、密送(MimeMessage.RecipientType.BCC)
        message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(receiveAddress,"收件人A","utf-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC,new InternetAddress(cReceiveAddress,"抄送人B","utf-8"));
//        message.setRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(mReceiveAddress,"密送人C","utf-8"));

        message.setSubject("这是标题(含有图片+附件)","utf-8");//设置标题

        //在邮件正文中加上图片
        //创建图片结点
        MimeBodyPart imagePart = new MimeBodyPart();
        DataHandler imageDataHandler = new DataHandler(new FileDataSource(new File("src/org/pjj/javamail/meiGui.jpg")));//图片地址
        imagePart.setDataHandler(imageDataHandler);
        imagePart.setContentID("meiGuiImage");//给图片结点设置id

        //创建文本结点: 目的是为了加载图片结点
        //因为图片在邮件中显示是,类似于在网页中显示,需要一个类似这样的<img src="路径" />来引入图片到邮件中 而<img.../>是个文本 所以需要创建文本结点
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent("这是邮件正文,,HelloWorld...<img src='cid:meiGuiImage' />","text/html;charset=utf-8");
        //因为之前设置图片结点时 已经将图片的路径传到图片结点中了, 所以此处 src=可以用 'cid:meiGuiImage' cid:图片结点的id

        //将 图片结点与文本结点 组装 成一个 复合结点
        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(imagePart);//加入图片结点
        mm_text_image.addBodyPart(textPart);//加入文本结点
        mm_text_image.setSubType("related");// 图片结点 与 文本结点 是个什么关系   此处是关联关系(related)

        //注意正文中只能出现 普通结点(MimeBodyPart) 不能出现 复合结点(MimeMultipart)
        //MimeMultipart --> MimeBodyPart
        MimeBodyPart text_image_bodyPart = new MimeBodyPart();
        text_image_bodyPart.setContent(mm_text_image);//将复合结点 转为 普通结点

        //简单表述下 上述过程: 将一个图片结点 和 一个文本结点 关联起来 然后转为一个 复合结点  然后将复合结点转为一个普通结点

        //发送邮件时 加上一个附件
        //附件结点
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler attachmentDataHandler = new DataHandler(new FileDataSource(new File("src/org/pjj/javamail/email.txt")));//将File类型的附件转为DataHandler类型
        attachment.setDataHandler(attachmentDataHandler);//将DataHandler类型的附件 设置到附件结点中
        attachment.setFileName(MimeUtility.encodeText(attachmentDataHandler.getName()));//获取附件的文件名  MimeUtility.encodeText()是为了防止乱码

        //将 之前处理好的"文本+图片"的复合结点转成的普通结点 与 附件结点  转为一个 复合结点
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text_image_bodyPart);//"文本+图片"的复合结点转成的普通结点
        mm.addBodyPart(attachment);//附件结点
        mm.setSubType("mixed"); //"文本+图片"的复合结点转成的普通结点 与 附件结点是什么关系  此处为混合关系

        //注意正文中只能出现 普通结点(MimeBodyPart) 不能出现 复合结点(MimeMultipart)
        //虽然 mm对象 是个复合结点 但是 已经不是只出现在正文了 mm一部分是附件

        message.setContent(mm,"text/html;charset=utf-8");//设置邮件正文.
        message.setSentDate(new Date());//设置发送时间
        message.saveChanges();//保存邮件

        return message;
    }
}
