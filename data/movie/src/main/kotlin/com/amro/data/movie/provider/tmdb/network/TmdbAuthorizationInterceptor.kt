package com.amro.data.movie.provider.tmdb.network

import com.amro.data.movie.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TmdbAuthorizationInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = BuildConfig.TMDB_ACCESS_TOKEN.trim()

        check(token.isNotBlank()) {
            "TMDB_ACCESS_TOKEN is missing. Configure it in your tmdb.properties according to the example"
        }

        val request = chain.request()
            .newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}