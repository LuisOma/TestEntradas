package com.example.newbase.data.entities

import com.google.gson.annotations.SerializedName

data class PostResults (
    @SerializedName("results"  ) var results  : List<PostDetail> = arrayListOf()
)