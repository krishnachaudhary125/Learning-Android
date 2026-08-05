package com.example.eSewaMarket.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.databinding.ItemLoadingBinding
import com.example.eSewaMarket.databinding.ItemProductBinding
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TYPE_ITEM = 0
private const val TYPE_LOADING = 1

class RecommendedProductAdapter(
    private val viewModel: ProductCountViewModel,
    private val onClick: (ProductResponse) -> Unit,
    private val onAddToCartClick: (String) -> Unit,
    private val onRemoveOneFromCartClick: (String) -> Unit,
    private val onFavouriteClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ProductResponse>()
    private var showLoadingFooter = false

    class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root){

        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        var quantityJob: Job? = null
        var favouriteJob: Job? = null
        }

    class LoadingViewHolder(val binding: ItemLoadingBinding) :
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

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ProductViewHolder) {
            holder.quantityJob?.cancel()
            holder.favouriteJob?.cancel()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            val product = items[position]
            val productId = product.id.toString()
            holder.binding.apply {
                productTitle.text = product.title
                brand.text = product.category.name
                price.text = product.price.toString()
                if(product.stock != 0){
                    soldOut.visibility = View.GONE
                    addCartBtn.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(root.context, R.color.green)
                    )
                    plusProduct.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(root.context, R.color.white)
                    )
                    plusProduct.isEnabled = true
                }else{
                    soldOut.visibility = View.VISIBLE
                    addCartBtn.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(root.context, R.color.addToCartSoldOut)
                    )
                    plusProduct.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(root.context, R.color.text_dark)
                    )
                    plusProduct.isEnabled = false
                }

                Glide.with(productImage.context)
                    .load(product.thumbnail)
                    .into(productImage)

                if (product.discountPercentage != null){
                    val dis = product.discountPercentage.toInt()
                    discount.text = "${dis}% OFF"
                    discount.visibility = View.VISIBLE
                }else{
                    discount.visibility = View.GONE
                }

                imageContainer.setOnClickListener {
                    onClick(product)
                }

                plusProduct.setOnClickListener {
                    onAddToCartClick(productId)
                }

                minusProduct.setOnClickListener {
                    onRemoveOneFromCartClick(productId)
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
                holder.favouriteJob = holder.scope.launch {
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
                    onFavouriteClick(productId)
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