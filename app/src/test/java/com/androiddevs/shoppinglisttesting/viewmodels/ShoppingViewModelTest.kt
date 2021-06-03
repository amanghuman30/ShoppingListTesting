package com.androiddevs.shoppinglisttesting.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttesting.data.models.Resource
import com.androiddevs.shoppinglisttesting.data.models.Status
import com.androiddevs.shoppinglisttesting.getOrAwaitValueTest
import com.androiddevs.shoppinglisttesting.repositories.MockShoppingRepository
import com.androiddevs.shoppinglisttesting.util.Constants
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {

    private lateinit var viewModel: ShoppingViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(MockShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field returns error`() {
        viewModel.insertShoppingItem("name","","10")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        Truth.assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name returns error`() {
        val str = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH+1) {
                append("a")
            }
        }
        viewModel.insertShoppingItem(str,"4","10")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        Truth.assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price returns error`() {
        val str = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1) {
                append("a")
            }
        }
        viewModel.insertShoppingItem("name","4",str)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        Truth.assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long amount returns error`() {
        viewModel.insertShoppingItem("name","999999999999999","20")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        Truth.assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item accurate values returns success`() {
        viewModel.insertShoppingItem("name","5","10")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        Truth.assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}
