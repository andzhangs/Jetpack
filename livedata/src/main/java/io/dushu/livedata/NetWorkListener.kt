package io.dushu.livedata

import android.annotation.SuppressLint
import android.content.Context
import android.net.*
import android.util.Log
import androidx.lifecycle.LiveData

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/3/13 15:34
 * @description 监听网络变化
 */
class NetWorkListener private constructor(private val mContext: Context) : LiveData<Network?>() {

    companion object {
        @JvmStatic
        fun getInstance(context: Context): NetWorkListener {
            return NetWorkListener(context.applicationContext)
        }
    }

    private val networkRequest = NetworkRequest.Builder().build()
    private var manager: ConnectivityManager =
        mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        Log.i("print_logs", "NetworkManager::onActive: ")
        manager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        Log.i("print_logs", "NetworkManager::onInactive: ")
        manager.unregisterNetworkCallback(networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.i("print_logs", "onAvailable: $network")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.i("print_logs", "onLosing: $network, $maxMsToLive")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.i("print_logs", "onLost: $network, ")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.i("print_logs", "onUnavailable: ")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            Log.i("print_logs", "onCapabilitiesChanged: $network, $networkCapabilities")

            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("print_logs", "onCapabilitiesChanged: WIFI通信 $network")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("print_logs", "onCapabilitiesChanged: 数据流量通信 $network")
                } else {
                    Log.i("print_logs", "onCapabilitiesChanged: 其他网络 $network")
                }
                getInstance(mContext).postValue(network)
                return
            }
            getInstance(mContext).postValue(null)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Log.i("print_logs", "onLinkPropertiesChanged: $network, $linkProperties")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            Log.i("print_logs", "onBlockedStatusChanged: $network, $blocked")
        }
    }

}