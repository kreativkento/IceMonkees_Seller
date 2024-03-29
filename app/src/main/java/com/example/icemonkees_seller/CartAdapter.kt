package com.example.icemonkees_seller

import CartData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context, // Corrected
    private val cartList: MutableList<CartData>, // Corrected
    private val deleteItem: (String) -> Unit // Assuming you pass document ID for deletion
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cartItem: CartData) {
            val title: TextView = itemView.findViewById(R.id.cartViewTitle)
            val price: TextView = itemView.findViewById(R.id.cartViewPrice)
            val image: ImageView = itemView.findViewById(R.id.cartViewProduct)

            title.text = cartItem.name
            price.text = "$${cartItem.price}"
            Glide.with(context).load(cartItem.imageUrl).into(image)

            val deleteButton: Button = itemView.findViewById(R.id.btn_cart_delete)
            deleteButton.setOnClickListener {
                deleteItem(cartItem.documentId) // Call deleteItem lambda with document ID
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

    override fun getItemCount() = cartList.size
}
