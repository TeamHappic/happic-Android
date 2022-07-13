package happy.kiki.happic.module.core.data.api.callAdapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MyCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

        if (returnType !is ParameterizedType) return null // same as Call<T>
        val containerType = getParameterUpperBound(0, returnType)

        if (getRawType(containerType) != NetworkResponse::class.java) return null
        if (containerType !is ParameterizedType) return null

        val bodyType = containerType.getBodyType()

        return when (getRawType(returnType)) {
            Call::class.java -> NetworkResponseAdapter<Any>(bodyType)
            else -> null
        }
    }

    private fun ParameterizedType.getBodyType() = getParameterUpperBound(0, this)
}