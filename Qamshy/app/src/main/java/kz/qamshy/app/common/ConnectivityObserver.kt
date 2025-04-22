package kz.qamshy.app.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ConnectivityObserver(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // 同步获取当前网络状态
    private fun getCurrentConnectivityStatus(): ConnectivityStatus {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            ConnectivityStatus.Available
        } else {
            ConnectivityStatus.Unavailable
        }
    }

    // 构建 Flow，先发出当前状态，然后再发出回调中的更新
    val status: Flow<ConnectivityStatus> = flow {
        // 首先发出当前的网络状态
        emit(getCurrentConnectivityStatus())
        // 然后合并后续变化
        emitAll(callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(ConnectivityStatus.Available)
                }

                override fun onLost(network: Network) {
                    trySend(ConnectivityStatus.Unavailable)
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        })
    }.conflate()
}