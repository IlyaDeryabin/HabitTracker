package ru.d3rvich.habittracker.data.remote.result_adapter

import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.d3rvich.habittracker.data.remote.result.HttpException
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import java.io.IOException

@Suppress("UNCHECKED_CAST")
class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, RetrofitResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<RetrofitResult<T>>) {
        proxy.enqueue(ResultCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<RetrofitResult<T>>
    ) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result: RetrofitResult<T>
            if (response.isSuccessful) {
                result = RetrofitResult.Success.HttpResponse(
                    value = response.body() as T,
                    statusCode = response.code(),
                    statusMessage = response.message(),
                    url = call.request().url.toString(),
                )
            } else {
                result = RetrofitResult.Failure.HttpError(
                    HttpException(
                        statusCode = response.code(),
                        statusMessage = response.message(),
                        url = call.request().url.toString(),
                    )
                )
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is retrofit2.HttpException -> RetrofitResult.Failure.HttpError(
                    HttpException(error.code(), error.message(), cause = error)
                )
                is IOException -> RetrofitResult.Failure.Error(error)
                else -> RetrofitResult.Failure.Error(error)
            }

            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout {
        return Timeout.NONE
    }
}