package com.kirwa.recipes

import android.content.Context
import android.net.ConnectivityManager


/**
 * Author by Cheruiyot Enock on 11/2/20.
 */

object NetworkUtil {
    fun hasNetwork(context: Context): Boolean {
        val activeNetworkInfo =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}