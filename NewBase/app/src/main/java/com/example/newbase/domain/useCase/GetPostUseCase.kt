package com.example.newbase.domain.useCase

import com.example.newbase.core.entity.Resource
import com.example.newbase.data.entities.PostDetail
import com.example.newbase.data.repo.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    fun getAllPostsRemote(): Flow<Resource<List<PostDetail>>> = postRepository.getPosts()
    suspend fun savePost(post: PostDetail) = postRepository.savePost(post)
}