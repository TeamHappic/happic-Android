package happy.kiki.happic.module.core.data.api.base

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import happy.kiki.happic.module.auth.util.AccessTokenUtil
import happy.kiki.happic.module.core.util.addFlipperNetworkInterceptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

object ApiServiceFactory {
    private val okHttpClient = OkHttpClient.Builder().addInterceptor {
        val originalReq = it.request()
        val newRequest = originalReq.newBuilder().addCommonHeaderValues().addAccessTokenHeaderValues().build()
        it.proceed(newRequest)
    }.addFlipperNetworkInterceptor().build()

    private fun Request.Builder.addCommonHeaderValues() = addHeader("Content-Type", "application/json")
    private fun Request.Builder.addAccessTokenHeaderValues() =
        AccessTokenUtil.getAccessToken()?.let { addHeader("x-auth-token", it) } ?: this

    @OptIn(ExperimentalSerializationApi::class)
    val _retrofit: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl("http://13.125.255.20:5001/") //http://10.0.2.2:3000/ http://13.125.255.20:5001/
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).client(okHttpClient)
                .build()
        }

    inline fun <reified T : Any> createService() = _retrofit.create(T::class.java)
}