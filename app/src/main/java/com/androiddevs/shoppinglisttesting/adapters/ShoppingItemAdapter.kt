package com.androiddevs.shoppinglisttesting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttesting.R
import com.androiddevs.shoppinglisttesting.data.local.ShoppingItem
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_shopping.view.*
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide : RequestManager
): RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems : List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(LayoutInflater.from(parent.context).
                    inflate(R.layout.item_shopping, parent, false))
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val item = shoppingItems[position]
        holder.itemView.apply {
            tvName.text = item.name
            tvShoppingItemAmount.text = item.amount.toString()
            tvShoppingItemPrice.text = item.price.toString()

            glide.load(item.imageUrl).into(ivShoppingImage)
        }
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    inner class ShoppingItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}
}