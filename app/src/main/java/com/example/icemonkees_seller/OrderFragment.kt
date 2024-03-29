package com.example.icemonkees_seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrderFragment : Fragment() {

    private var orderList: MutableList<OrderData> = mutableListOf()
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        recyclerView = view.findViewById(R.id.orderRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 4) // Use 4 columns in the grid

        // Correctly initialize OrderAdapter with the context, order list, and an onDataChangedListener implementation
        orderAdapter = OrderAdapter(requireContext(), orderList, object : OrderAdapter.OnDataChangedListener {
            override fun onDataChanged() {
                // Optional: Implement actions to take when data changes, like refreshing the data
            }
        })

        recyclerView.adapter = orderAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchOrders() // Ensure this is called to fetch data
    }

    private fun fetchOrders() {
        FirebaseFirestore.getInstance().collection("products")
            .get()
            .addOnSuccessListener { documents ->
                orderList.clear() // Clear the list first to avoid duplicating items
                for (document in documents) {
                    val order = document.toObject(OrderData::class.java)
                    orderList.add(order)
                }
                orderAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
            }
    }
}

