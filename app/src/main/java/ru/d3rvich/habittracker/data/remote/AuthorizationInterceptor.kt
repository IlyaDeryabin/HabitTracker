package ru.d3rvich.habittracker.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import ru.d3rvich.habittracker.BuildConfig

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}