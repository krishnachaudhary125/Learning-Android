package com.example.atry.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atry.R
import com.example.atry.data.models.Product
import com.example.atry.data.models.ProductResponse
import com.example.atry.databinding.ItemLoadingBinding
import com.example.atry.databinding.ItemProductBinding

private const val TYPE_ITEM = 0
private const val TYPE_LOADING = 1
var numProduct = 0

class RecommendedProductAdapter(
    private val onClick: (ProductResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ProductResponse>()
    private var showLoadingFooter = false

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoadingViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (showLoadingFooter && position == itemCount - 1) TYPE_LOADING else TYPE_ITEM
    }

    override fun getItemCount(): Int = items.size + if (showLoadingFooter) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_LOADING) {
            LoadingViewHolder(ItemLoadingBinding.inflate(inflater, parent, false))
        } else {
            ProductViewHolder(ItemProductBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            val product = items[position]
            holder.binding.apply {
                productTitle.text = product.title
                brand.text = product.category
                price.text = product.price.toString()

                Glide.with(productImage.context)
                    .load(product.thumbnail)
                    .into(productImage)

                imageContainer.setOnClickListener { onClick(product) }

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
            }
        }
        when (holder) {
            is LoadingViewHolder -> {
                Glide.with(holder.binding.root)
                    .asGif()
                    .load(R.drawable.loading)
                    .into(holder.binding.loading)
            }
        }
    }
    fun submitFullList(newItems: List<ProductResponse>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setLoading(loading: Boolean) {
        if (showLoadingFooter == loading) return
        showLoadingFooter = loading
        if (loading) {
            notifyItemInserted(itemCount - 1)
        } else {
            notifyItemRemoved(itemCount)
        }
    }

    fun isLoadingFooterShown() = showLoadingFooter
}