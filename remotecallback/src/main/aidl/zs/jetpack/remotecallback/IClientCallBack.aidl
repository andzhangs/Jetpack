// IClientCallBack.aidl
package zs.jetpack.remotecallback;

// Declare any non-default types here with import statements

interface IClientCallBack {

   // 服务端调用客户端的回调
   void onReceived(String msg);

}