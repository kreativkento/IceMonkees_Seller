package com.example.icemonkees_seller

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class OrderAdapter(
    private val context: Context,
    private val orderList: List<OrderData>,
    private val onDataChangedListener: OnDataChangedListener
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    interface OnDataChangedListener {
        fun onDataChanged()
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: OrderData) {
            Glide.with(itemView.context).load(order.imageUrl).into(itemView.findViewById(R.id.orderViewProduct))
            itemView.findViewById<TextView>(R.id.orderViewTitle).text = order.name
            itemView.findViewById<TextView>(R.id.orderViewPrice).text = "$${order.price}"

            itemView.setOnClickListener {
                AlertDialog.Builder(context).apply { // 'context' is now accessible
                    setTitle("Add to Cart")
                    setMessage("Are you sure you want to add this item to the cart?")
                    setPositiveButton("Add to Cart") { dialog, which ->
                        addToCart(order)
                    }
                    setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    show()

                }
            }
        }

        private fun addToCart(order: OrderData) {
            val db = FirebaseFirestore.getInstance()
            val tempCollectionRef = db.collection("temp")

            val tempOrderData = hashMapOf(
                "imageUrl" to order.imageUrl,
                "name" to order.name,
                "price" to order.price
            )

            tempCollectionRef.add(tempOrderData)
                .addOnSuccessListener {
                    Log.d("OrderAdapter", "Item added to cart with ID: ${it.id}")
                    onDataChangedListener.onDataChanged() // Trigger the callback
                }
                .addOnFailureListener { e ->
                    Log.w("OrderAdapter", "Error adding item to cart", e)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount() = orderList.size


}

