package com.example.icemonkees_seller

import CartData
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemonkees_seller.databinding.FragmentCartBinding
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var cartList: MutableList<CartData> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchCartItems()

        return binding.root
    }

    fun refreshCartFragment() {
        fetchCartItems()
    }

    private fun setupRecyclerView() {
        binding.cartRecyclerView.layoutManager = GridLayoutManager(context, 1)
        cartAdapter = CartAdapter(requireContext(), cartList)
        binding.cartRecyclerView.adapter = cartAdapter
    }

    fun fetchCartItems() {
        FirebaseFirestore.getInstance().collection("temp")
            .orderBy("id")  // Order the results by the 'id' field
            .get()
            .addOnSuccessListener { documents ->
                cartList.clear()
                documents.forEach { document ->
                    val cartItem = document.toObject(CartData::class.java)
                    cartList.add(cartItem)
                }
                cartAdapter.notifyDataSetChanged()  // Notify the adapter that data has changed
            }
            .addOnFailureListener { e ->
                Log.e("CartFragment", "Error fetching cart items", e)
            }
    }


    private fun deleteItem(documentId: Any) {

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

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Cancel Order")
            setMessage("Are you sure to cancel order? This would discard all items added in your cart.")
            setPositiveButton("Clear") { dialog, which ->
                deleteAllItemsInCollection()
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cartItem: CartData) {
            // Binding logic
        }
    }

}
