package io.dushu.lifcycle.lifecycleservice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * author: zhangshuai 6/26/21 6:37 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class MyLocationObserver constructor(private val context: Context) : DefaultLifecycleObserver {

    private lateinit var mLocationManager: LocationManager
    private var mLocationListener: MyLocationListener? = null

    init {
        Log.i("print_log", "MyLocationObserver is init.")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i("print_log", "startLocation：")
        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("print_log", "apply permission：")
            Toast.makeText(this.context, "赶紧去申请权限", Toast.LENGTH_SHORT).show()
            return
        }
        mLocationManager =
            this.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener = MyLocationListener()
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000L,
            1F,
            mLocationListener!!
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.i("print_log", "stopLocation：")
        mLocationListener?.let {
            mLocationManager.removeUpdates(it)
        }
    }

    private class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.i("print_log", "onLocationChanged：$location")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.i("print_log", "onStatusChanged：")
        }

        override fun onProviderEnabled(provider: String) {
            Log.i("print_log", "onProviderEnabled：")
        }

        override fun onProviderDisabled(provider: String) {
            Log.i("print_log", "onProviderDisabled：")
        }
    }

}