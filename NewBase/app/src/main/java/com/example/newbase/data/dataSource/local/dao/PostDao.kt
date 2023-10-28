package com.example.newbase.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newbase.data.entities.PostDetail

@Dao
interface PostDao {

   @Query("Select * from PostDetail")
   fun getAllPosts(): List<PostDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(posts: List<PostDetail>)

}