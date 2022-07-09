package org.pjj.socket1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) throws Exception {

        //绑定服务的端口 , ip默认是本机的ip      向外界暴露了一个服务, 该服务的地址: 本机ip+端口9999
        ServerSocket server = new ServerSocket(9999);
        while(true){
            Socket socket = server.accept();//阻塞式等待连接   (线程会一直阻塞在这里, 等待连接了才会执行下面的代码)
            System.out.println("与客户端建立连接一个连接");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedOutputStream bos = null;
                    BufferedInputStream fileBis = null;//获取文件输入流
                    try {
                        //准备要发送的数据
                        File file = new File("E:/IDEA/ideaProject/JavaCommonApplications/data/YF.jpg");

                        fileBis = new BufferedInputStream(new FileInputStream(file));//获取文件输入流,用来给谁客户端发送文件
                        bos = new BufferedOutputStream(socket.getOutputStream());//获取输出流,用来给客户端,发送消息

                        byte[] fileByte = new byte[1024];
                        int len = -1;
                        while((len=fileBis.read(fileByte)) != -1){
                            bos.write(fileByte,0,len);
                        }
                        bos.flush();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        //关闭流
                        try {
                            if(fileBis != null){
                                fileBis.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(bos != null){
                                bos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(socket != null){
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }
}
