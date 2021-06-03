package com.androiddevs.shoppinglisttesting.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.androiddevs.shoppinglisttesting.data.models.PixbayResponse
import com.androiddevs.shoppinglisttesting.data.models.Resource
import com.androiddevs.shoppinglisttesting.repositories.ShoppingRepository
import com.androiddevs.shoppinglisttesting.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
        private val shoppingRepository: ShoppingRepository
    ) : ViewModel(){

    val shoppingItems = shoppingRepository.observeAllShoppingItems()

    val totalPrice = shoppingRepository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<PixbayResponse>>>()
    val images : MutableLiveData<Event<Resource<PixbayResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl : MutableLiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus : MutableLiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImage(url  : String) {
        _curImageUrl.postValue(url)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.insertShoppingItem(shoppingItem)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name : String, amountString : String, priceString : String) {

    }

    fun searchImages(imageQuery : String) {

    }
}