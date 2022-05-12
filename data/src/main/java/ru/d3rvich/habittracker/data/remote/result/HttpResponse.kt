package ru.d3rvich.habittracker.data.remote.result

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}