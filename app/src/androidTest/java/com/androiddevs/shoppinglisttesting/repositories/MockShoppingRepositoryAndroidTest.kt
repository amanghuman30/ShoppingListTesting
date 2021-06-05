package com.androiddevs.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.androiddevs.shoppinglisttesting.data.models.PixbayResponse
import com.androiddevs.shoppinglisttesting.data.models.Resource

class MockShoppingRepositoryAndroidTest : ShoppingRepository{

    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>()
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false


    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice() : Float {
        return shoppingItems.sumOf { it.price.toDouble()}.toFloat()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImages(imageQuery: String): Resource<PixbayResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error Occurred!", null)
        } else {
            Resource.success(PixbayResponse(listOf(), 0, 0))
        }
    }
}