package org.pjj.socket1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) throws Exception {
        //客户端 连接服务端发布的服务  连接服务端需要服务端的ip和port    通过构造方法传入    由于ip就是我本机,所以可以使用localhost
        Socket client = new Socket("localhost",9999);

        //客户端接收服务端发来的消息
        BufferedInputStream bis = new BufferedInputStream(client.getInputStream());//获取输入流,用来接收,服务端发送过来的消息
        BufferedOutputStream fileBos = new BufferedOutputStream(new FileOutputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/CopyYF.jpg"));
        byte[] data = new byte[1024];
        int len = -1;
        while((len=bis.read(data)) != -1){
            fileBos.write(data,0,len);
        }
        fileBos.flush();

        //关闭流
        fileBos.close();
        bis.close();
        client.close();
    }
}
