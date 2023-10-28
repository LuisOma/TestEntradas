package com.example.newbase.data.repo

import com.example.newbase.core.entity.Resource
import com.example.newbase.data.dataSource.local.DatabaseBuilder
import com.example.newbase.data.dataSource.remote.PostRemoteDataSource
import com.example.newbase.data.entities.PostDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource
) {
    fun getPosts(): Flow<Resource<List<PostDetail>>> = flow {
        val responseStatus = postRemoteDataSource.getPostFirebase()
        if (responseStatus.status == Resource.Status.SUCCESS) {
            responseStatus.data?.let { posts ->
                DatabaseBuilder.getInstance().postDao().insertPost(posts)
                emit(Resource(Resource.Status.SUCCESS, posts, ""))
            }
        } else if (responseStatus.status == Resource.Status.ERROR) {
            // Emitir desde otra fuente de datos; en este caso, una función que devuelve Flow
            emitAll(getPostsLocal())
        }
    }.flowOn(Dispatchers.IO)

    fun getPostsLocal(): Flow<Resource<List<PostDetail>>> = flow {
        val responseStatus = DatabaseBuilder.getInstance().postDao().getAllPosts()
        emit(Resource(Resource.Status.SUCCESS, responseStatus, ""))
    }.flowOn(Dispatchers.IO)


    suspend fun savePost(post: PostDetail) = postRemoteDataSource.addPost(post)
}