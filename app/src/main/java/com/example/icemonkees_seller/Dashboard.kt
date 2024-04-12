package com.example.icemonkees_seller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.icemonkees_seller.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

interface ProductClickListener {
    fun onProductClicked(product: ProductsData)
    fun refreshCartFragment()
}

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var homeFragment: HomeFragment? = null
    private var productsFragment: ProductsFragment? = null
    private var cartFragment: CartFragment? = null
    private var transactionsFragment: TransactionsFragment? = null
    private var inventoryFragment: InventoryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragments()
        setupBottomNavigationView()
    }

    private fun setupFragments() {
        homeFragment = HomeFragment()
        productsFragment = ProductsFragment().apply {
            productClickListener = object : ProductClickListener {
                override fun onProductClicked(product: ProductsData) {
                    // Implement action
                }
                override fun refreshCartFragment() {
                    cartFragment?.fetchCartItems()
                }
            }
        }
        cartFragment = CartFragment()
        transactionsFragment = TransactionsFragment()
        inventoryFragment = InventoryFragment()

        // Add all fragments in the initial transaction
        supportFragmentManager.beginTransaction().apply {
            // Always add and immediately show the homeFragment
            add(R.id.flFragmentHome, homeFragment!!, "HOME_FRAGMENT")
            add(R.id.flFragmentPanel1, productsFragment!!, "PRODUCTS_FRAGMENT").hide(productsFragment!!)
            add(R.id.flFragmentPanel2, cartFragment!!, "CART_FRAGMENT").hide(cartFragment!!)
            // Assuming other fragments are commented out or handled similarly
            commit()
        }

        // Ensure homeFragment is visible and brought to front immediately upon initialization
        showFragment(homeFragment!!)
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    showFragment(homeFragment!!)
                    true
                }
                R.id.navigation_sale -> {
                    // Show productsFragment and cartFragment for sale navigation
                    supportFragmentManager.beginTransaction().apply {
                        setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        hideAllFragments()
                        show(productsFragment!!)
                        show(cartFragment!!)
                        commit()
                    }
                    true
                }
                R.id.navigation_transactions -> {
                    showFragment(transactionsFragment!!)
                    true
                }
                R.id.navigation_inventory -> {
                    showFragment(inventoryFragment!!)
                    true
                }
                R.id.navigation_logout -> {
                    showLogoutConfirmationDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun hideAllFragments() {
        // Hide all fragments
        supportFragmentManager.beginTransaction().apply {
            listOf(homeFragment, productsFragment, cartFragment, transactionsFragment, inventoryFragment).forEach {
                if (it != null && it.isAdded) {
                    hide(it)
                }
            }
        }.commit()
    }


    private fun showFragment(fragmentToShow: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)

        // Hide all fragments
        listOf(homeFragment, productsFragment, cartFragment, transactionsFragment, inventoryFragment).forEach {
            if (it != null && it.isAdded) {
                transaction.hide(it)
            }
        }

        // Show the selected fragment
        transaction.show(fragmentToShow)

        // If homeFragment is shown, ensure it is brought to the front
        if (fragmentToShow == homeFragment) {
            binding.flFragmentHome.bringToFront()
        }

        transaction.commit()
    }


    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout_confirmation_title))
            setMessage(getString(R.string.logout_confirmation_message))
            setPositiveButton(getString(R.string.confirm)) { _, _ ->
                logoutUser()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}
