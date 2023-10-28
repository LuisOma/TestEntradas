package com.example.newbase.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.example.newbase.BuildConfig.BASE_URL
import com.example.newbase.data.dataSource.remote.PostRemoteDataSource
import com.example.newbase.data.dataSource.remote.PostService
import com.example.newbase.data.repo.PostRepository
import com.example.newbase.domain.useCase.GetPostUseCase
import com.example.newbase.util.NetworkStateDelegate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url()
            .newBuilder()
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return@Interceptor chain.proceed(request)
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun providePostService(retrofit: Retrofit): PostService = retrofit.create(PostService::class.java)

    @Singleton
    @Provides
    fun providePostRemoteDataSource(postService: PostService, firestore: FirebaseFirestore) = PostRemoteDataSource(postService, firestore)

    @Singleton
    @Provides
    fun providePostRepository(remoteDataSource: PostRemoteDataSource) =
        PostRepository(remoteDataSource)

    @Singleton
    @Provides
    fun provideGetPostUseCase(postRepository: PostRepository) =
        GetPostUseCase(postRepository)

    @Singleton
    @Provides
    fun provideNetworkStateDelegate(@ApplicationContext context: Context): NetworkStateDelegate {
        return NetworkStateDelegate(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


}