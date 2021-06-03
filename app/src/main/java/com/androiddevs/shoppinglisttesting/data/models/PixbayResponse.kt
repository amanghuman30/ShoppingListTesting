package com.androiddevs.shoppinglisttesting.data.models

data class PixbayResponse(
    val hits: List<Hit>?,
    val total: Int?,
    val totalHits: Int?
)