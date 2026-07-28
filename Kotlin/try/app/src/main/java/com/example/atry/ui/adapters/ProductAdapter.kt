package com.example.atry.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atry.R
import com.example.atry.data.models.Product
import com.example.atry.databinding.ItemProductBinding

class ProductAdapter(
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    var numProduct = 0
    var isFavourite = false
    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var productList: List<Product>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount() = productList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.apply {
            val product = productList[position]
            productTitle.text = product.title
            brand.text = product.category
            price.text = product.price.toString()

            Glide.with(productImage.context)
                .load(product.thumbnail)
                .into(productImage)

            imageContainer.setOnClickListener {
                onClick(product)
            }

            plusProduct.setOnClickListener {
                numProduct+=1
                numOfProduct.text = numProduct.toString()
                minusProduct.visibility = View.VISIBLE
                numOfProduct.visibility = View.VISIBLE
            }
            minusProduct.setOnClickListener {
                if (numProduct > 0) {
                    numProduct--
                    numOfProduct.text = numProduct.toString()
                }

                if (numProduct == 0) {
                    minusProduct.visibility = View.GONE
                    numOfProduct.visibility = View.GONE
                }
            }

            favourite.setOnClickListener {
                isFavourite = !isFavourite

                favourite.setImageResource(
                    if(isFavourite){
                        R.drawable.ic_fav_filled
                    }else{
                        R.drawable.ic_fav
                    }
                )
            }
        }
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }
}