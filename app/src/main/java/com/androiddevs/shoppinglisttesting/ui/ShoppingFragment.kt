package com.androiddevs.shoppinglisttesting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.adapters.ShoppingItemAdapter
import com.androiddevs.shoppinglisttesting.viewmodels.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    private val shoppingItemAdapter: ShoppingItemAdapter,
    private var viewModel: ShoppingViewModel? = null
) : Fragment(R.layout.fragment_shopping){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }

        setUpRecyclerView()
        setUpObservers()
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = shoppingItemAdapter.shoppingItems[viewHolder.adapterPosition]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(requireView(), "Item deleted successfully!", Snackbar.LENGTH_SHORT).apply {
                setAction("undo") {
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }

    private fun setUpObservers() {
        viewModel?.shoppingItems?.observe(viewLifecycleOwner, {
            shoppingItemAdapter.shoppingItems = it
        })

        viewModel?.totalPrice?.observe(viewLifecycleOwner, {
            val price = it ?: 0f
            val priceText = "Total price : $price$"
            tvShoppingItemPrice.text = priceText
        })
    }

    private fun setUpRecyclerView() {
        rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}