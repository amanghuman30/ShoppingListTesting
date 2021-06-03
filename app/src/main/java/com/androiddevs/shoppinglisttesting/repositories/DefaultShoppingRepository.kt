package com.androiddevs.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttesting.api.PixabayApi
import com.androiddevs.shoppinglisttesting.data.local.ShoppingDao
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.androiddevs.shoppinglisttesting.data.models.PixbayResponse
import com.androiddevs.shoppinglisttesting.data.models.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayApi: PixabayApi) : ShoppingRepository{

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImages(imageQuery: String): Resource<PixbayResponse> {
        return try {
            val response = pixabayApi.searchForImages(imageQuery)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown Error Occurred!", null)
            } else {
                Resource.error("An Unknown Error Occurred!", null)
            }
        } catch (e : Exception) {
            Resource.error("Couldn't reach server. Check your internet connection please", null)
        }
    }


}