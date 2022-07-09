package org.pjj.remote.procedure.call.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    /**
     * 获取代表服务端接口的的动态代理对象(HelloService)
     * @param serviceInterface 通过反射类型来获取 请求的接口名
     * @param addr 待请求服务端的 ip和端口 InetSocketAddress这样类中就是会包含 ip 和 端口
     * @param <T> 返回值类型 是个泛型<T> T  调用时 传个什么 返回值就是什么类型
     * @return
     */
    public static <T> T getRemoteProxyObj(Class serviceInterface, InetSocketAddress addr){
        //Proxy.newProxyInstance(a,b,c)
        /**
         * a: 类加载器: 需要代理哪个类(例如HelloService接口), 就需要将HelloService的类加载器 传入第一个参数
         * b: 需要代理的对象 具备那些方法 --接口
         */
        InvocationHandler handler = new InvocationHandler() {
            //proxy 代理的对象       method 哪个方法(sayHello(参数列表))     args  方法的参数列表
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
                //客户端向服务端发送请求: 请求某一个具体的接口
                Socket socket = new Socket();
                ObjectOutputStream output = null;
                ObjectInputStream input = null;
                Object result = null;
                try {
                    socket.connect(addr);   //向服务端发起连接 的ip+端口  都包含在这个对象中addr
                    output = new ObjectOutputStream(socket.getOutputStream());//发送 序列化流(对象流)
                    //客户端需要向服务端发送 下面这些参数 然后服务端根据这些参数 返回一个(等下填空)
                    //接口名、方法名
                    //方法参数的类型、方法参数
                    output.writeUTF(serviceInterface.getName());//接口名
                    output.writeUTF(method.getName());//方法名
                    output.writeObject(method.getParameterTypes());//方法参数的类型
                    output.writeObject(args);//方法参数
                    //等待服务端处理....

                    //接收服务器处理后的结果(返回值)
                    input = new ObjectInputStream(socket.getInputStream());//接收
                    result = input.readObject();//客户端 -> 服务端 -> 客户端

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    try {
                        if(input != null){
                            input.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(output != null){
                            output.close();
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
                return result;
            }
        };


        return (T)Proxy.newProxyInstance(serviceInterface.getClassLoader(),new Class[]{serviceInterface},handler);
    }
}
