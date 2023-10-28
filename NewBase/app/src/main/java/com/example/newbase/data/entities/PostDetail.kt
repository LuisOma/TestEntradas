package com.example.newbase.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "PostDetail")
data class PostDetail (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String? = null,
    @ColumnInfo(name = "author")
    @SerializedName("author")
    var author: String? = null,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    var date: String? = null,
    @ColumnInfo(name = "content")
    @SerializedName("content")
    var content: String? = null
)