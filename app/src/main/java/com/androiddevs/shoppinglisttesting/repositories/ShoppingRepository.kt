package com.androiddevs.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.androiddevs.shoppinglisttesting.data.models.PixbayResponse
import com.androiddevs.shoppinglisttesting.data.models.Resource
import retrofit2.Response

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems() : LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchForImages(imageQuery : String) : Resource<PixbayResponse>

}