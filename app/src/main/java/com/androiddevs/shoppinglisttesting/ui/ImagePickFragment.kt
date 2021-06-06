package com.androiddevs.shoppinglisttesting.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.adapters.ImageAdapter
import com.androiddevs.shoppinglisttesting.data.models.Status
import com.androiddevs.shoppinglisttesting.util.Constants
import com.androiddevs.shoppinglisttesting.viewmodels.ShoppingViewModel
import kotlinx.android.synthetic.main.fragment_image_pick.*
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
) : Fragment(R.layout.fragment_image_pick){

    lateinit var viewModel : ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }
        setUpRecyclerView()

        etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(str: Editable?) {
                viewModel.searchImages(str.toString())
            }
        })
        setUpObservers()

        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.clearImageSearch()
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)


    }

    private fun setUpRecyclerView() {
        rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
        }
    }

    private fun setUpObservers() {
        viewModel.images.observe(viewLifecycleOwner, { response ->
            val result = response.getContentIfNotHandled()
            when(result?.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    result.data?.hits?.let {
                        val images = it.map { it.previewURL }
                        imageAdapter.images = images as List<String>
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),result.message,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}