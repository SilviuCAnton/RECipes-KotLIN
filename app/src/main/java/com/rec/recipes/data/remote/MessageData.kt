package com.rec.recipes.data.remote

import com.rec.recipes.data.Item

data class MessageData(var event: String, var payload: RecipeJson) {
    data class RecipeJson(var item: Item)
}