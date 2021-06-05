package com.androiddevs.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.androiddevs.shoppinglisttesting.data.models.Resource
import com.androiddevs.shoppinglisttesting.data.models.Status
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
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var shoppingFactory : ShoppingFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun clickItemImageView_navigateToImagePickFragment() {
        var navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.ivShoppingImage)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment())
    }

    @Test
    fun pressBackButton_popBackStack() {
        var navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()

        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun pressBackButton_CurrentImageUrlEmptiesInViewModel() {
        val navController = Mockito.mock(NavController::class.java)

        val testViewModel = ShoppingViewModel(MockShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        Espresso.pressBack()

        Truth.assertThat(testViewModel?.curImageUrl?.value).isEqualTo("")
    }

    @Test
    fun clickAddShoppingItem_insertsItemInDb() {
        val testViewModel = ShoppingViewModel(MockShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment> (fragmentFactory = shoppingFactory) {
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemName)).perform(ViewActions.replaceText("Shopping item"))
        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemAmount)).perform(ViewActions.replaceText("5"))
        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemPrice)).perform(ViewActions.replaceText("5.5"))
        Espresso.onView(ViewMatchers.withId(R.id.btnAddShoppingItem)).perform(ViewActions.click())

        val result = testViewModel.shoppingItems.getOrAwaitValue()

        Truth.assertThat(result).contains(
            ShoppingItem("Shopping item",5,5.5f, "")
        )
    }

    @Test
    fun clickAddShoppingItem_insertsItemInDbCheckInsertStatus() {
        val testViewModel = ShoppingViewModel(MockShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment> (fragmentFactory = shoppingFactory) {
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemName)).perform(ViewActions.replaceText("Shopping item"))
        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemAmount)).perform(ViewActions.replaceText("5"))
        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemPrice)).perform(ViewActions.replaceText("5.5"))
        Espresso.onView(ViewMatchers.withId(R.id.btnAddShoppingItem)).perform(ViewActions.click())

        val insertStatus = testViewModel.insertShoppingItemStatus.getOrAwaitValue()

        Truth.assertThat(insertStatus.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}