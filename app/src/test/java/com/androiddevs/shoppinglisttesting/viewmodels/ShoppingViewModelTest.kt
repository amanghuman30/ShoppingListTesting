package com.androiddevs.shoppinglisttesting.viewmodels

import com.androiddevs.shoppinglisttesting.repositories.MockShoppingRepository
import org.junit.Before
import org.junit.Test

class ShoppingViewModelTest {

    private lateinit var viewModel: ShoppingViewModel


    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(MockShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field returns error`() {
        
    }

}
