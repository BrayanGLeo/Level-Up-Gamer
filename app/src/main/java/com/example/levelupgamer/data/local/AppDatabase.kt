package com.example.levelupgamer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.levelupgamer.data.model.CartItem
import com.example.levelupgamer.data.model.OrderEntity
import com.example.levelupgamer.data.model.OrderItemEntity

@Database(entities = [CartItem::class, OrderEntity::class, OrderItemEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "level_up_gamer_db"
                )
                .fallbackToDestructiveMigration() // Simplifica cambios de esquema en desarrollo
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}