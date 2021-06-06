package com.androiddevs.shoppinglisttesting.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.androiddevs.shoppinglisttesting.data.models.PixbayResponse
import com.androiddevs.shoppinglisttesting.data.models.Resource
import com.androiddevs.shoppinglisttesting.data.models.Status
import com.androiddevs.shoppinglisttesting.repositories.ShoppingRepository
import com.androiddevs.shoppinglisttesting.util.Constants
import com.androiddevs.shoppinglisttesting.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
        private val shoppingRepository: ShoppingRepository
    ) : ViewModel(){

    val shoppingItems = shoppingRepository.observeAllShoppingItems()

    val totalPrice = shoppingRepository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<PixbayResponse>>>()
    val images : LiveData<Event<Resource<PixbayResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl : LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    private var searchJob : Job? = null

    fun setCurImageUrl(url  : String) {
        _curImageUrl.postValue(url)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.insertShoppingItem(shoppingItem)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name : String, amountString : String, priceString : String) {
        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Fields must not be empty", null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The name of the item must not exceed ${Constants.MAX_NAME_LENGTH} characters",
                null)))
            return
        }

        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The price of the item must not exceed ${Constants.MAX_PRICE_LENGTH} characters",
                null)))
            return
        }

        val amount = try {
            amountString.toInt()
        } catch (e : Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "Please enter valid amount",
                null)))
            return
        }

        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), _curImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchImages(imageQuery : String) {
        if(imageQuery.isEmpty()) {
            return
        }

        _images.value = Event(Resource.loading(null))

        searchJob?.apply {
            if(isActive)
                cancel()
        }

        searchJob = viewModelScope.launch {
            val response = shoppingRepository.searchForImages(imageQuery)
            _images.value = Event(response)
        }
    }

    fun clearImageSearch() {
        _images.postValue(Event(Resource(Status.LOADING, null, null)))
    }
}