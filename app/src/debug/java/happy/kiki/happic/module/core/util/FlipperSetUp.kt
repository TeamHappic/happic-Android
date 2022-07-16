package happy.kiki.happic.module.core.util

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import happy.kiki.happic.BuildConfig
import okhttp3.OkHttpClient

val networkPlugin = NetworkFlipperPlugin()

fun Application.setUpFlipper() {
    SoLoader.init(this, false)
    if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {

        AndroidFlipperClient.getInstance(this).apply {
            addPlugin(InspectorFlipperPlugin(this@setUpFlipper, DescriptorMapping.withDefaults()))
            addPlugin(networkPlugin)
        }.start()
    }
}

fun OkHttpClient.Builder.addFlipperNetworkInterceptor(): OkHttpClient.Builder {
    return addNetworkInterceptor(FlipperOkhttpInterceptor(networkPlugin))
}