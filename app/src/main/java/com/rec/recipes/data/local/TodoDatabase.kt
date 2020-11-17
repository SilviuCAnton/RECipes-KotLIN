package com.rec.recipes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.rec.recipes.data.Item

@Database(entities = [Item::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

//                @kotlinx.coroutines.InternalCoroutinesApi()
        fun getDatabase(context: Context, scope: CoroutineScope): TodoDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "recipesdb3"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        print("DB is open!!");
                    }
                }
            }
        }
    }

}
