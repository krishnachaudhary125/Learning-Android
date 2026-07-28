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
import com.example.atry.ui.viewmodel.ProductCountViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class ProductAdapter(
    private val viewModel: ProductCountViewModel,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        var quantityJob: Job? = null
        var favouriteJob: Job? = null
    }

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
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount() = productList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onViewRecycled(holder: ProductViewHolder) {
        super.onViewRecycled(holder)
        holder.quantityJob?.cancel()
        holder.favouriteJob?.cancel()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = productList[position]
        val productId = product.id.toString()

        holder.binding.apply {

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
                viewModel.addToCart(productId)
            }

            minusProduct.setOnClickListener {
                viewModel.removeOneFromCart(productId)
            }

            holder.quantityJob?.cancel()
            holder.quantityJob = holder.scope.launch {
                viewModel.quantityOf(productId).collect { qty ->

                    numOfProduct.text = qty.toString()

                    val visible = if (qty > 0) View.VISIBLE else View.GONE
                    numOfProduct.visibility = visible
                    minusProduct.visibility = visible
                }
            }

            holder.favouriteJob?.cancel()
            holder.scope.launch {
                viewModel.isFavourite(productId).collect { isFav ->
                    favourite.setImageResource(
                        if (isFav)
                            R.drawable.ic_fav_filled
                        else
                            R.drawable.ic_fav
                    )
                }
            }

            favourite.setOnClickListener {
                holder.scope.launch {
                    val isFav = viewModel.isFavourite(productId).first()

                    if (isFav) {
                        viewModel.removeFromFavourites(productId)
                    } else {
                        viewModel.addToFavourites(productId)
                    }
                }
            }
        }
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }
}