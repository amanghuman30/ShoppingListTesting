package com.androiddevs.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttesting.api.PixabayApi
import com.androiddevs.shoppinglisttesting.data.local.ShoppingDao
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItemDatabase
import com.androiddevs.shoppinglisttesting.repositories.DefaultShoppingRepository
import com.androiddevs.shoppinglisttesting.repositories.ShoppingRepository
import com.androiddevs.shoppinglisttesting.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(@ApplicationContext app : Context) =
        Room.databaseBuilder(app, ShoppingItemDatabase::class.java, Constants.SHOPPING_DATABASE_NAME)

    @Provides
    @Singleton
    fun provideShoppingDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Provides
    @Singleton
    fun providePixabayApi() : PixabayApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(pixabayApi: PixabayApi, shoppingDao: ShoppingDao) =
        DefaultShoppingRepository(shoppingDao, pixabayApi) as ShoppingRepository

}