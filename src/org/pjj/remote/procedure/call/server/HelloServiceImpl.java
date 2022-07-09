package org.pjj.remote.procedure.call.server;

import java.io.Serializable;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHi(String name) {
        System.out.println("Hi,"+name);
        return "Hi,"+name;
    }
}
