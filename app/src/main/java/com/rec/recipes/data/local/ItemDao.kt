package com.rec.recipes.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rec.recipes.data.Item

@Dao
interface ItemDao {
    @Query("SELECT * from recipe WHERE userId=:userId ORDER BY name ASC")
    fun getAll(userId: String): LiveData<List<Item>>

    @Query("SELECT * FROM recipe WHERE _id=:id ")
    fun getById(id: String): LiveData<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Item)

    @Query("DELETE FROM recipe WHERE _id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM recipe")
    suspend fun deleteAll()
}