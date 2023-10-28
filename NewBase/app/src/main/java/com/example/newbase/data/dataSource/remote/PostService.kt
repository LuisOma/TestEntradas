package com.example.newbase.data.dataSource.remote

import com.example.newbase.data.entities.PostDetail
import retrofit2.Response
import retrofit2.http.GET

interface PostService {
    @GET("urlposts")
    suspend fun getPostsList(
    ): Response<List<PostDetail>>

}