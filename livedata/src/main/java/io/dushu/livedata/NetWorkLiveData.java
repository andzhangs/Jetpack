package io.dushu.livedata;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;


/**
 * @author zhangshuai
 * 监听网络变化
 */
public class NetWorkLiveData extends LiveData<NetworkInfo> {

    private final Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static NetWorkLiveData sNetWorkLiveData;
    private NetworkReceiver mReceiver;
    private final IntentFilter mIntentFilter;

    static NetWorkLiveData getInstance(Context context) {
        if (sNetWorkLiveData == null) {
            sNetWorkLiveData=new NetWorkLiveData(context);
        }
        return sNetWorkLiveData;
    }

    private NetWorkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mReceiver=new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onActive() {
        super.onActive();
        mContext.registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mContext.unregisterReceiver(mReceiver);
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            getInstance(context).setValue(networkInfo);
        }
    }

}
