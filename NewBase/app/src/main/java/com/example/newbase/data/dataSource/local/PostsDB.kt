package com.example.newbase.data.dataSource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newbase.data.dataSource.local.dao.PostDao
import com.example.newbase.data.entities.PostDetail

@Database(entities = [PostDetail::class], version = 1)
abstract class PostsDB : RoomDatabase() {
    abstract fun postDao(): PostDao
}

object DatabaseBuilder {
    private var INSTANCE: PostsDB? = null

    fun getInstance(): PostsDB {
        return INSTANCE ?: throw IllegalStateException("Database not initialized!")
    }

    fun initDB(context: Context){
        synchronized(PostsDB::class) {
            if (INSTANCE == null) {
                INSTANCE = buildRoomDB(context)
            }
        }
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            PostsDB::class.java,
            "my-database"
        ).build()
}