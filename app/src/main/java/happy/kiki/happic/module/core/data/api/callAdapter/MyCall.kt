package happy.kiki.happic.module.core.data.api.callAdapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class MyCall<T>(
    private val backingCall: Call<T>, private val bodyType: Type
) : Call<NetworkResponse<T>> {
    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        backingCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                return callback.onResponse(this@MyCall, Response.success(response.asNetworkResponse(bodyType)))
            }

            override fun onFailure(call: Call<T>, t: Throwable) =
                callback.onResponse(this@MyCall, Response.success(t.asNetworkResponse()))
        })
    }

    override fun execute(): Response<NetworkResponse<T>> =
        Response.success(backingCall.execute().asNetworkResponse(bodyType))

    override fun clone() = MyCall(backingCall, bodyType)

    override fun isExecuted(): Boolean = backingCall.isExecuted

    override fun cancel() = backingCall.cancel()

    override fun isCanceled(): Boolean = backingCall.isCanceled

    override fun request(): Request = backingCall.request()

    override fun timeout(): Timeout = backingCall.timeout()
}