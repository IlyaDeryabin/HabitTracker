package ru.d3rvich.habittracker.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", apiKey)
            .build()
        return chain.proceed(request)
    }
}