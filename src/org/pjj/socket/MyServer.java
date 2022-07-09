package org.pjj.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
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
public class MyServer {
    public static void main(String[] args) throws Exception {

        //绑定服务的端口 , ip默认是本机的ip      向外界暴露了一个服务, 该服务的地址: 本机ip+端口9999
        ServerSocket server = new ServerSocket(9999);

        Socket socket = server.accept();//阻塞式等待连接   (线程会一直阻塞在这里, 等待连接了才会执行下面的代码)
        System.out.println("与客户端建立连接成功");


        //服务端向客户端发送消息
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());//获取输出流,用来给客户端,发送消息
        String str = "hello";
        bos.write(str.getBytes());
        bos.flush();
//        bos.close();

        //服务端接收客户端的反馈(接口客户端发来的消息)
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());//获取输入流,接口客户端发送的消息
        byte[] data = new byte[50];
        
        bis.read(data);
        str = new String(data);//把byte[]型的数据解码成字符串String
        System.out.println("----------"+data.length);
        System.out.println("接收到客户端的反馈(接收客户端发送的消息):"+str);
//        bis.close();


        bis.close();
        bos.close();
        socket.close();
        server.close();

    }
}
