package com.androiddevs.shoppinglisttesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttesting.getOrAwaitValue
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : ShoppingItemDatabase
    private lateinit var shoppingDao: ShoppingDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java).allowMainThreadQueries().build()
        shoppingDao = database.shoppingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1,1f,"url", id=1)
        shoppingDao.insertShoppingItem(shoppingItem)

        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()

        Truth.assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteSHoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1,1f,"url", id=1)
        shoppingDao.insertShoppingItem(shoppingItem)

        shoppingDao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()

        Truth.assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name1", 1,1f,"url", id=1)
        val shoppingItem2 = ShoppingItem("name2", 2,2f,"url", id=2)
        val shoppingItem3 = ShoppingItem("name3", 3,3f,"url", id=3)
        shoppingDao.insertShoppingItem(shoppingItem1)
        shoppingDao.insertShoppingItem(shoppingItem2)
        shoppingDao.insertShoppingItem(shoppingItem3)

        val totalSum = shoppingDao.observeTotalPrice().getOrAwaitValue()

        Truth.assertThat(totalSum).isEqualTo(1+2*2f+3*3f)
    }

}