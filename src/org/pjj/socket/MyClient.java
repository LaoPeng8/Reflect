package org.pjj.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 记录一下 报这个异常是因为 java.net.SocketException: Connection reset
 * 1，如果一端的Socket被关闭（或主动关闭，或因为异常退出而 引起的关闭），另一端仍发送数据，发送的第一个数据包引发该异常(Connect reset by peer)。
 * 2，一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常（Connection reset）。简单的说就是在连接断开后的读和写操作引起的。
 * 这里是因为 MyServer.java结束运行了,但并没有close() 而MyClient.java还在读取数据 所以抛异常  我在MyServer.java加上close()后就没有报这个异常了.
 *
 * 记录一下 报这个异常是因为 Exception in thread "main" java.net.SocketException: Socket is closed
 * (明明没有关闭socket  socket.close()) 却报 Socket is closed
 * 后来才知道 socket 只要在 io流close的情况下 自动关闭，意思就是你想边发送边接受最正确的方式就是发送和 接受的操作都做完之后 再一起关闭IO流 完美解决。
 */
public class MyClient {
    public static void main(String[] args) throws Exception {
        //客户端 连接服务端发布的服务  连接服务端需要服务端的ip和port    通过构造方法传入    由于ip就是我本机,所以可以使用localhost
        Socket client = new Socket("localhost",9999);

        //客户端接收服务端发来的消息
        BufferedInputStream bis = new BufferedInputStream(client.getInputStream());//获取输入流,用来接收,服务端发送过来的消息
        byte[] data = new byte[5];
        bis.read(data);
        String str=new String(data,0,data.length);//把byte[]型的数据解码成字符串String
        System.out.println("接收到服务端发送的消息:"+str);
//        bis.close();


        //客户端向服务端做出反馈(向服务端发送消息)
        BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());//获取输出流,用来向服务端,发送消息
        str = "Hello World";
        bos.write(str.getBytes(),0,str.length());
        bos.flush();
//        bos.close();

        bos.close();
        bis.close();
        client.close();
    }
}
