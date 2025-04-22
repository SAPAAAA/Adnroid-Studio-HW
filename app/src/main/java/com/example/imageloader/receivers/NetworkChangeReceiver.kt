package com.example.imageloader.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.imageloader.utils.NetworkUtils

class NetworkChangeReceiver : BroadcastReceiver() {
	interface NetworkStateListener {
		fun onNetworkAvailable()
		fun onNetworkUnavailable()
	}
	
	companion object {
		private const val TAG = "NetworkChangeReceiver"
		var listener: NetworkStateListener? = null
		
		fun setNetworkStateListener(listener: NetworkStateListener?) {
			NetworkChangeReceiver.listener = listener
		}
	}
	
	@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION && context != null) {
			val isConnected = NetworkUtils.isNetworkAvailable(context)
			Log.d(TAG, "Network state changed via BroadcastReceiver. Connected: $isConnected")
			
			listener?.let {
				if (isConnected) {
					it.onNetworkAvailable()
				} else {
					it.onNetworkUnavailable()
				}
			} ?: Log.d(TAG, "NetworkStateListener is null")
		}
	}
}