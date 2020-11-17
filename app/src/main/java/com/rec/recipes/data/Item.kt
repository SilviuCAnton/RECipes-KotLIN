package com.rec.recipes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
data class Item(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "calories") var calories: Int,
    @ColumnInfo(name = "userId") var userId: String
) {
    override fun toString(): String = name
}