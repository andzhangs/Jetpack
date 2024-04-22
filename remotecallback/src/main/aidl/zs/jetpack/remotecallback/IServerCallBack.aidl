// IServerCallBack.aidl
package zs.jetpack.remotecallback;

// Declare any non-default types here with import statements
import zs.jetpack.remotecallback.IClientCallBack;

//客户端调用服务的方法
interface IServerCallBack {

    // 向服务端注册客户端回调
    void register(IClientCallBack callback);

    // 向服务端注销客户端回调
    void unregister(IClientCallBack callback);

    // 向服务端发送消息
    void callServer(String msg);

}