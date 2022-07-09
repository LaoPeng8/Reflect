package org.pjj.remote.procedure.call.server;

import java.io.Serializable;

public interface HelloService extends Serializable {
    String sayHi(String name);// hi name
}
