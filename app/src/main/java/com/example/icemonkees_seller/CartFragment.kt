package com.example.icemonkees_seller

import CartData
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private var cartList: MutableList<CartData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = GridLayoutManager(context, 1)
        cartAdapter = CartAdapter(requireContext(), cartList) { documentId ->
            deleteItem(documentId)
        }
        cartRecyclerView.adapter = cartAdapter

        fetchCartItems()

        return view
    }

    private fun deleteItem(documentId: Any) {

    }

    private fun fetchCartItems() {
        FirebaseFirestore.getInstance().collection("temp")
            .get()
            .addOnSuccessListener { documents ->
                cartList.clear() // Clear existing items
                for (document in documents) {
                    val item = document.toObject(CartData::class.java).copy(documentId = document.id)
                    cartList.add(item)
                }
                cartAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle errors, e.g., logging or showing a user message
            }
    }


    private fun showInitializingDialogAndDelete() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("Initializing...")
            .setCancelable(false) // Prevent dismissal on touch outside
            .create()

        dialog.show()

        // Close dialog after 3 seconds (3000 milliseconds)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            // Optional: Call a method here if you want to perform any action right after closing the dialog.
            // For instance, deleting all items in the "temp" collection.
            deleteAllItemsInCollection()
        }, 3000)
    }

    private fun deleteAllItemsInCollection() {
        // Fetch all documents in the "temp" collection
        FirebaseFirestore.getInstance().collection("temp")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Delete each document
                    FirebaseFirestore.getInstance().collection("temp").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            // Handle successful deletion, e.g., log or UI update
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                        }
                }
                // Optional: Refresh the list in UI after deletion
                cartList.clear()
                cartAdapter.notifyDataSetChanged()
            }
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cartItem: CartData) {
            // Binding logic

            val deleteButton: Button = itemView.findViewById(R.id.btn_cart_delete)
            deleteButton.setOnClickListener {
                deleteItem(cartItem.documentId) // This now correctly references the lambda
            }
        }
    }


}
