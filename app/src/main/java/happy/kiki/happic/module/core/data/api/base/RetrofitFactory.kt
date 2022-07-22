package happy.kiki.happic.module.core.data.api.base

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import happy.kiki.happic.module.auth.util.JwtUtil
import happy.kiki.happic.module.core.util.addFlipperNetworkInterceptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

val globalJson = Json {
    ignoreUnknownKeys = true
}

object ApiServiceFactory {
    private val okHttpClient = OkHttpClient.Builder().addInterceptor {
        val originalReq = it.request()
        val newRequest = originalReq.newBuilder().addCommonHeaderValues().addAccessTokenHeaderValues().build()
        it.proceed(newRequest)
    }.addFlipperNetworkInterceptor().build()

    private fun Request.Builder.addCommonHeaderValues() = addHeader("Content-Type", "application/json")
//    private fun Request.Builder.addAccessTokenHeaderValues() =
//        JwtUtil.load()?.let { addHeader("Authorization", it) } ?: this

    private fun Request.Builder.addAccessTokenHeaderValues() = addHeader("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiNjJkOTk2Y2Q1N2MzNTQzYjM2N2EwM2E4In0sImlhdCI6MTY1ODQzOTI2MSwiZXhwIjoxNjU5NjQ4ODYxfQ.2SRL-VydGLvfpz5feh-rGC76rPGQpBOOo9apP72IFNo")

    @OptIn(ExperimentalSerializationApi::class)
    val _retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://3.39.169.83:5001") //http://10.0.2.2:3000/ http://3.39.169.83:5001
            .addConverterFactory(globalJson.asConverterFactory("application/json".toMediaType())).client(okHttpClient)
            .build()

    inline fun <reified T : Any> createService() = _retrofit.create(T::class.java)
}