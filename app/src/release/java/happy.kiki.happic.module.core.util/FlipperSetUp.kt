package happy.kiki.happic.module.core.util

import android.app.Application

fun Application.setUpFlipper() = 0

fun OkHttpClient.Builder.addFlipperNetworkInterceptor(): OkHttpClient.Builder {
    return this
}