package happy.kiki.happic.module.core.data.api.base

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
        val newRequest = originalReq.newBuilder().addCommonHeaders().build()
        it.proceed(newRequest)
    }.addFlipperNetworkInterceptor().build()

    private fun Request.Builder.addCommonHeaders() = addHeader("Content-Type", "application/json")

    @OptIn(ExperimentalSerializationApi::class)
    val _retrofit: Retrofit = Retrofit.Builder().baseUrl("http://13.125.255.20:5001/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).client(okHttpClient).build()

    inline fun <reified T : Any> createService() = _retrofit.create(T::class.java)
}