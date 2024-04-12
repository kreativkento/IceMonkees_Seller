package com.example.icemonkees_seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.icemonkees_seller.databinding.ItemProductBinding // Corrected import

class ProductsAdapter(
    private var products: List<ProductsData>,
    productClickListener: ProductClickListener
) :
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Updated to use ItemProductBinding
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    fun updateData(newProducts: List<ProductsData>) {
        products = newProducts
        notifyDataSetChanged()
    }

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductsData) {
            Glide.with(itemView.context)
                .load(product.imageUrl)
                .into(binding.productsViewProduct) // Make sure ID matches XML

            binding.productsViewTitle.text = product.name
            binding.productsViewPrice.text = String.format("₱%.2f", product.price)
        }
    }
}
