package com.example.icemonkees_seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.icemonkees_seller.databinding.ItemProductBinding // Corrected import
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class ProductsAdapter(
    private var products: List<ProductsData>,
    private val productClickListener: ProductClickListener
) :
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, productClickListener, products)
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

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val productClickListener: ProductClickListener,
        private val products: List<ProductsData>
    ) : RecyclerView.ViewHolder(binding.root) {
        private val firestore = FirebaseFirestore.getInstance()

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = products[position]
                    addProductToFirestore(product)
                }
            }
        }

        fun bind(product: ProductsData) {
            Glide.with(itemView.context)
                .load(product.imageUrl)
                .into(binding.productsViewProduct)

            binding.productsViewTitle.text = product.name
            binding.productsViewPrice.text = String.format("₱%.2f", product.price)
        }

        private fun addProductToFirestore(product: ProductsData) {
            val counterRef = firestore.collection("utils").document("counter") // Document to store the current counter
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(counterRef)
                var currentId = snapshot.getLong("currentId") ?: 0 // Get the current ID or start at 0 if it's null
                currentId += 1 // Increment the ID for the new document

                // Prepare the data to be saved in Firestore
                val productData = hashMapOf(
                    "id" to currentId,
                    "imageURL" to product.imageUrl,
                    "product" to product.name,
                    "price" to product.price,
                    "timestamp" to FieldValue.serverTimestamp() // Add the current timestamp
                )

                // Update the counter in Firestore
                transaction.set(counterRef, hashMapOf("currentId" to currentId))

                // Add the new product data to the temp collection
                transaction.set(firestore.collection("temp").document(), productData)
            }.addOnSuccessListener {
                productClickListener.onProductClicked(product)
            }.addOnFailureListener { e ->
                // Log error or show a toast here
            }
        }
    }
}
