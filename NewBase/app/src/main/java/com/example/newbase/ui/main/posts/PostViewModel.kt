package com.example.newbase.ui.main.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.newbase.core.entity.Resource
import com.example.newbase.data.entities.PostDetail
import com.example.newbase.domain.useCase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
): ViewModel() {
    var navController: NavController? = null
    val posts: MutableLiveData<Resource<List<PostDetail>>> = MutableLiveData()
    var selectedPost: PostDetail? = null
    var savePostStatus: MutableLiveData<Boolean?> = MutableLiveData(null)

    fun savePost(post: PostDetail){
        viewModelScope.launch {
            val res = getPostUseCase.savePost(post)
            withContext(Dispatchers.Main){
                if (res.data == true)
                    savePostStatus.postValue(true)
            }
        }
    }

    fun getPosts() {
        viewModelScope.launch {
            getPostUseCase.getAllPostsRemote().collect { result ->
                posts.value = result
            }
        }
    }
}
