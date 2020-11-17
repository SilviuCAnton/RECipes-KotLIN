package com.rec.recipes.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.rec.core.Api
import com.rec.core.Constants
import com.rec.recipes.data.local.ItemDao
import com.rec.core.Result
import com.rec.core.TAG
import com.rec.recipes.data.remote.ItemApi
import com.rec.recipes.data.remote.MessageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemRepository(private val itemDao: ItemDao) {

    val recipes = MediatorLiveData<List<Item>>().apply { postValue(emptyList()) }

    init {
        CoroutineScope(Dispatchers.Main).launch { collectEvents() }
    }

    suspend fun refresh(): Result<Boolean> {
        try {
            val items = ItemApi.service.find()
            recipes.value = items;
            for (item in items) {
                itemDao.insert(item)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            Log.v(TAG, "Suntem in offline!");
            val userId = Constants.instance()?.fetchValueString("_id")
            Log.v(TAG, "Avem ID $userId");
            recipes.addSource(itemDao.getAll(userId!!)){
                recipes.value = it;
            }
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<Item> {
        return itemDao.getById(itemId)
    }

    suspend fun save(item: Item): Result<Item> {
        try {
            val createdItem = ItemApi.service.create(item)
            itemDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: Item): Result<Item> {
        try {
            val updatedItem = ItemApi.service.update(item._id, item)
            itemDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun delete(itemId: String): Result<Boolean> {
        try {
            ItemApi.service.delete(itemId)
            itemDao.delete(itemId)
            return Result.Success(true)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    private suspend fun collectEvents() {
        while (true) {
            val messageData = Gson().fromJson(Api.eventChannel.receive(), MessageData::class.java)
            Log.d("GLF: collectEvents", "received $messageData")
            handleMessage(messageData)
        }
    }

    private suspend fun handleMessage(messageData: MessageData) {
        val game = messageData.payload.item
        when (messageData.event) {
            "created" -> itemDao.insert(game)
            "updated" -> itemDao.update(game)
            "deleted" -> itemDao.delete(game._id)
            else -> {
                Log.d("GLF: handleMessage", "received $messageData")
            }
        }
    }
}