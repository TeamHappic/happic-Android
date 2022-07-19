package happy.kiki.happic.module.core.util

import android.app.Application
import okhttp3.OkHttpClient

fun Application.setUpFlipper() = 0

fun OkHttpClient.Builder.addFlipperNetworkInterceptor(): OkHttpClient.Builder {
    return this
}