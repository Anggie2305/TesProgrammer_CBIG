package com.example.tes_cbig.main

import androidx.lifecycle.LiveData
import com.example.tes_cbig.api.ApiClient
import com.example.tes_cbig.database.Item
import com.example.tes_cbig.database.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ItemRepository(private val itemDao: ItemDao){

    val allItems: LiveData<List<Item>> = itemDao.getAllItems()

    suspend fun refreshItems(token: String): Result<List<Item>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.getItems("Bearer $token").awaitResponse()
                if (response.isSuccessful && response.body()?.statusCode == 1) {
                    val items = response.body()?.data ?: emptyList()
                    // Simpan ke database lokal
                    itemDao.deleteAllItems()
                    itemDao.insertAllItems(items.map { Item.fromItem(it) })
                    Result.success(items)
                } else {
                    Result.failure(Exception(response.message()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            } as Result<List<Item>>
        }
    }

    suspend fun insert(item: Item) {
        itemDao.insertItems(item)
    }

    suspend fun update(item: Item) {
        itemDao.updateItem(item)
    }

    suspend fun delete(item: Item) {
        itemDao.deleteItem(item)
    }
}
