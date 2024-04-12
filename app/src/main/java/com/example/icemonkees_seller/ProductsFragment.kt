package com.example.icemonkees_seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.icemonkees_seller.databinding.FragmentProductsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    var productClickListener: ProductClickListener? = null
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = Firebase.firestore
        initRecyclerView()
        fetchProducts()
    }

    private fun initRecyclerView() {
        binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 4)
        productsAdapter = ProductsAdapter(listOf(), productClickListener!!)
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun fetchProducts() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val productList = documents.map { document ->
                    document.toObject(ProductsData::class.java)
                }
                productsAdapter.updateData(productList)
            }
            .addOnFailureListener { exception ->
                // Handle error, log or show error message
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
