package org.pjj.remote.procedure.call.test;

import org.pjj.remote.procedure.call.client.Client;
import org.pjj.remote.procedure.call.server.HelloService;

import java.net.InetSocketAddress;

public class RPCClientTest {


    public static void main(String[] args) {
        try {
            HelloService service = Client.getRemoteProxyObj(Class.forName("org.pjj.remote.procedure.call.server.HelloService"),new InetSocketAddress("localhost",9999));
            System.out.println(service.sayHi("zs"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
