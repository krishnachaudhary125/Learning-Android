package com.example.atry.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atry.data.models.Product
import com.example.atry.databinding.ItemProductBinding

class FeaturedProductAdapter(
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<FeaturedProductAdapter.FeaturedProductViewHolder>() {

    inner class FeaturedProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var product: List<Product>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun getItemCount() = product.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedProductViewHolder {
        return FeaturedProductViewHolder(ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FeaturedProductViewHolder, position: Int) {
        holder.binding.apply {
            val products = product[position]
            productTitle.text = products.title
            brand.text = products.brand
            price.text = products.price.toString()

            Glide.with(productImage.context)
                .load(products.image)
                .into(productImage)

            root.setOnClickListener {
                onClick(products)
            }
        }
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }
}