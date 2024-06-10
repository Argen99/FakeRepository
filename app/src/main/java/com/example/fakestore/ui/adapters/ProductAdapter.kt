package com.example.fakestore.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.model.Product
import com.example.fakestore.core.extensions.loadImageWithGlide
import com.example.fakestore.databinding.ItemProductBinding

class ProductAdapter(
    private val onItemClick: (id: Int) -> Unit
): ListAdapter<Product, ProductAdapter.ProductViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { product -> holder.onBind(product) }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding): ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(product: Product): Unit = with(binding) {
            ivProduct.loadImageWithGlide(product.image)
            tvTitle.text = product.title
            tvPrice.text = "${product.price}$"
        }

        init {
            binding.root.setOnClickListener {
                onItemClick(getItem(adapterPosition).id)
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