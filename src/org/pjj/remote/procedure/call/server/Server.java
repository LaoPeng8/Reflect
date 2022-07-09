package org.pjj.remote.procedure.call.server;

/**
 * 服务中心接口
 */
public interface Server {
    void start();//开启服务中心
    void stop();//关闭服务中心
    void register(Class service,Class serviceImpl);//注册服务
    //....
}
