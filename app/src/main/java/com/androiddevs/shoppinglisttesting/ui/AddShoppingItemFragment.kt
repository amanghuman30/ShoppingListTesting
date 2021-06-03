package com.androiddevs.shoppinglisttesting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.viewmodels.ShoppingViewModel

class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item){

    lateinit var viewModel : ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
    }

}