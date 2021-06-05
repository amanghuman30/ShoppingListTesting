package com.androiddevs.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.adapters.ShoppingItemAdapter
import com.androiddevs.shoppinglisttesting.getOrAwaitValue
import com.androiddevs.shoppinglisttesting.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttesting.repositories.MockShoppingRepositoryAndroidTest
import com.androiddevs.shoppinglisttesting.viewmodels.ShoppingViewModel
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class ShoppingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Inject
    lateinit var shoppingFragmentFactory: TestShoppingFragmentFactory

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun swipeRecyclerItem_deletesFromDb() {
        var testViewModel : ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory = shoppingFragmentFactory) {
            testViewModel = viewModel
            testViewModel?.insertShoppingItem("name", "5", "5.5")
        }

        Espresso.onView(ViewMatchers.withId(R.id.rvShoppingItems))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                ViewActions.swipeLeft()
            ))

        Truth.assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun clickAddShoppingItemFab_navigateToAddShoppingItemFragment() {

        var navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<ShoppingFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.fabAddShoppingItem)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment())
    }
}