package com.yoyopab.todo.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object Api {
    private const val TOKEN = "d06bbc41a12269a66f51f8c4a2600129c6e144e8"

    private val retrofit by lazy {
        // client HTTP
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        // transforme le JSON en objets kotlin et inversement
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        // instance retrofit pour implémenter les webServices:
        Retrofit.Builder()
            .baseUrl("https://api.todoist.com/")
            .client(okHttpClient)
            .addConverterFactory(jsonSerializer.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val userWebService : UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService : TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}