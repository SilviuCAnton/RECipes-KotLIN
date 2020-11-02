package com.rec.recipes.data.remote

import retrofit2.http.*
import com.rec.core.Api
import com.rec.recipes.data.Item

object ItemApi {
    interface Service {
        @GET("/item")
        suspend fun find(): List<Item>

        @GET("/item/{id}")
        suspend fun read(@Path("id") itemId: String): Item;

        @Headers("Content-Type: application/json")
        @POST("/item")
        suspend fun create(@Body item: Item): Item

        @Headers("Content-Type: application/json")
        @PUT("/item/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Item): Item
    }

    val service: Service = Api.retrofit.create(
        Service::class.java)
}