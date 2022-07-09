package org.pjj.remote.procedure.call.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务中心的实现类
 */
public class ServerCenter implements Server {

    //map: 服务端的所有 可供客户端访问的接口, 都注册到该map中.
    //key:接口的名字"HelloService"   value="真正的HelloService的实现"
    private static HashMap<String,Class> serviceRegister = new HashMap<String,Class>();


    private static final String LOCALHOST = "localhost";//服务端ip
    private static int port = 9999;//服务端 端口号(默认9999也可以通过构造器传入)
    //连接池: 连接池中存在多个连接对象, 每个连接对象都可以处理一个客户请求
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private boolean isRunning = false;//代表服务是否开启.

    public ServerCenter(){
    }
    public ServerCenter(int port){
        this.port = port;
    }

    //开启服务端服务
    @Override
    public void start() {   //while(true){start();}

        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(LOCALHOST,port));//设置服务端的ip和端口
            isRunning = true;//开启服务
            System.out.println("start  server...");
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(isRunning){
            try {
                socket = server.accept();//等待客户端连接
                executor.execute(new ServiceTask(socket)); //启动线程 去处理客户请求
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void stop() {//关闭服务
        isRunning = false;
        executor.shutdown();

    }

    //将服务注册到 map中       key是接口名     value是该接口的具体实现.
    @Override
    public void register(Class service,Class serviceImpl) {
        serviceRegister.put(service.getName(),serviceImpl);
    }

    private static class ServiceTask implements Runnable {

        private Socket socket = null;

        public ServiceTask(){
        }
        public ServiceTask(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {

                //接收到客户端连接即请求, 处理该请求...
                input = new ObjectInputStream(socket.getInputStream());//序列化流
                //因为ObjectInputStream 发送是什么顺序 接收就必须是什么顺序 所以我们需要安装发送的顺序接收
                String serviceName = input.readUTF();//接口名
                String methodName = input.readUTF();//方法名
                Class[] parameterTypes = (Class[]) input.readObject();//方法参数的类型
                Object[] arguments = (Object[])input.readObject();//方法参数

                //根据客户请求, 到map中, 找到与之对应的 具体接口
                Class serviceClass = serviceRegister.get(serviceName);//HelloService
                Method method = serviceClass.getMethod(methodName, parameterTypes);//根据客户请求 找到具体接口之后, 找到需要调用该接口中的, 哪个方法
                //执行该方法
                Object result = method.invoke(serviceClass.getConstructor().newInstance(), arguments);

                //根据客户请求, 处理完毕后, 将方法返回值 还给客户端
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(output != null){
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(input != null){
                        input.close();
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
    }


}
