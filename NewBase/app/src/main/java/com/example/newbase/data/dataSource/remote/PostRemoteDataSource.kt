package com.example.newbase.data.dataSource.remote

import android.content.ContentValues.TAG
import android.util.Log
import com.example.newbase.MyApp
import com.example.newbase.core.entity.Resource
import com.example.newbase.data.entities.PostDetail
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Response
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val postService: PostService,
    private val firestore: FirebaseFirestore
) : BaseRemoteDataSource() {

    //Método necesario para obtener entradas con un consumo REST
    suspend fun getPosts() = getResult { postService.getPostsList() }

    //Método necesario para guardar una entrada con un consumo REST
    suspend fun savePost(): Response<Boolean>{
        return Response.success(true)
    }

    suspend fun getPostFirebase(): Resource<List<PostDetail>> = suspendCancellableCoroutine { continuation ->
        val notes = mutableListOf<PostDetail>()
        firestore.collection("entries")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val note = document.toObject(PostDetail::class.java)
                    notes.add(note)
                }
                continuation.resume(Resource.success(notes), null)
            }
            .addOnFailureListener { exception ->
                continuation.resume(Resource.error("Error al obtener documentos: $exception"), null)
            }
    }

    suspend fun addPost(note: PostDetail): Resource<Boolean> = suspendCancellableCoroutine { continuation ->
        firestore.collection("entries")
            .add(note)
            .addOnSuccessListener {
                continuation.resume(Resource.success(true), null)
            }
            .addOnFailureListener { exception ->
                continuation.resume(Resource.error("error", false), null)
            }
    }



}