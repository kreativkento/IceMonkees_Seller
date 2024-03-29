package com.example.icemonkees_seller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.icemonkees_seller.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var homeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationView()
        // Initially load the HomeFragment exclusively in its container
        homeFragment = HomeFragment().also {
            supportFragmentManager.beginTransaction()
                .add(R.id.flFragmentHome, it, "HOME_FRAGMENT")
                .commit()
        }
        // Initially, make sure HomeFragment's container is at the front
        binding.flFragmentHome.bringToFront()
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    homeFragment?.let {
                        // Custom animations for showing the HomeFragment
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .show(it)
                            .commit()
                        binding.flFragmentHome.bringToFront()
                    }
                    true
                }
                R.id.navigation_sale -> {
                    loadOtherFragment(OrderFragment())
                    loadCartFragment(CartFragment())
                    true

                }
                R.id.navigation_transactions -> {
                    loadOtherFragment(TransactionsFragment())
                    true
                }
                R.id.navigation_inventory -> {
                    loadOtherFragment(InventoryFragment())
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

    // Load and display non-home fragments, hiding HomeFragment if visible
    private fun loadOtherFragment(fragment: Fragment) {
        homeFragment?.let {
            // Hide HomeFragment when other fragments are selected
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .hide(it)
                .commit()
        }
        // Replace the non-home fragment in its designated container
        supportFragmentManager.beginTransaction()
            // Apply a built-in transition for replacing fragments
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.flFragmentFirst, fragment)
            .commit()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout_confirmation_title)) // Make sure these strings are defined in your strings.xml
            setMessage(getString(R.string.logout_confirmation_message))
            setPositiveButton(getString(R.string.confirm)) { dialog, which ->
                logoutUser()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun logoutUser() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut()

        // Navigate to SignInActivity
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun loadCartFragment(fragment: Fragment) {
        // Create an instance of CartFragment
        val cartFragment = CartFragment()

        // Begin a transaction to replace the contents of the flFragmentSecond with CartFragment
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // Optional: Set custom animations for the transaction
            replace(R.id.flFragmentSecond, cartFragment) // Replace flFragmentSecond's contents with CartFragment
            commit() // Commit the transaction
        }
    }
}
