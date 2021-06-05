package com.androiddevs.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.adapters.ImageAdapter
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


@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun clickImage_popbackStackSetImageUrl() {

        var navController = Mockito.mock(NavController::class.java)

        val imageUrl = "test"

        val testViewModel = ShoppingViewModel(MockShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Mockito.verify(navController).popBackStack()
        Truth.assertThat(testViewModel.curImageUrl.value).isEqualTo(imageUrl)
    }

}