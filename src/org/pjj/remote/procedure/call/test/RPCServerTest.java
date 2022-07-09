package org.pjj.remote.procedure.call.test;

import org.pjj.remote.procedure.call.server.HelloService;
import org.pjj.remote.procedure.call.server.HelloServiceImpl;
import org.pjj.remote.procedure.call.server.Server;
import org.pjj.remote.procedure.call.server.ServerCenter;

public class RPCServerTest {
    public static void main(String[] args) {

        new Thread(()->{
            //服务中心
            Server server = new ServerCenter(9999);

            //将HelloService.class及实现类 注册到服务中心
            server.register(HelloService.class, HelloServiceImpl.class);

            server.start();//开启服务器
        }).start();

    }
}
