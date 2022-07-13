package happy.kiki.happic.module.core.data.api.callAdapter

import com.google.gson.JsonSyntaxException
import happy.kiki.happic.module.core.data.api.callAdapter.NetworkResponse.Error
import happy.kiki.happic.module.core.data.api.callAdapter.NetworkResponse.JsonParsingError
import happy.kiki.happic.module.core.data.api.callAdapter.NetworkResponse.NetworkError
import happy.kiki.happic.module.core.data.api.callAdapter.NetworkResponse.ServerError
import happy.kiki.happic.module.core.data.api.callAdapter.NetworkResponse.Success
import happy.kiki.happic.module.core.data.api.callAdapter.NetworkResponse.UnknownError
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

internal fun <T> Response<T>.asNetworkResponse(
    bodyType: Type
): NetworkResponse<T> {
    return if (isSuccessful) {
        parseSuccessfulResponse(this, bodyType)
    } else {
        parseUnsuccessfulResponse()
    }
}

private fun <T> parseSuccessfulResponse(response: Response<T>, successType: Type): NetworkResponse<T> {
    val responseBody: T? = response.body()
    if (responseBody == null) {
        if (successType === Unit::class.java) {
            @Suppress("UNCHECKED_CAST") return Success(Unit) as NetworkResponse<T>
        }
        return ServerError()
    }
    return Success(responseBody)
}

private fun <T> parseUnsuccessfulResponse(): Error<T> {
    return ServerError()
}

internal fun <T> Throwable.asNetworkResponse(): NetworkResponse<T> {
    return when (this) {
        is IOException -> NetworkError()
        is HttpException -> ServerError()
        is JsonSyntaxException -> JsonParsingError()
        else -> UnknownError()
    }
}