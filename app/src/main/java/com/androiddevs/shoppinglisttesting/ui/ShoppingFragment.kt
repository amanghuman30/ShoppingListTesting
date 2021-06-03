package com.androiddevs.shoppinglisttesting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.viewmodels.ShoppingViewModel

class ShoppingFragment : Fragment(R.layout.fragment_shopping){

    lateinit var viewModel : ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
    }

}