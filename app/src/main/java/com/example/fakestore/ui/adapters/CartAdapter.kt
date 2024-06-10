package com.example.fakestore.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.model.Product
import com.example.fakestore.core.extensions.loadImageWithGlide
import com.example.fakestore.databinding.ItemCartBinding

class CartAdapter(
    private val onRemoveItemClick: (product: Product) -> Unit
): ListAdapter<Product, CartAdapter.CartViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder = CartViewHolder(
        ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        getItem(position)?.let { product -> holder.onBind(product) }
    }

    inner class CartViewHolder(private val binding: ItemCartBinding): ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(product: Product): Unit = with(binding) {
            ivProductImage.loadImageWithGlide(product.image)
            tvTitle.text = product.title
            tvPrice.text = product.price.toString()
        }

        init {
            binding.btnRemove.setOnClickListener {
                onRemoveItemClick(getItem(adapterPosition))
            }
        }
    }

    companion object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}