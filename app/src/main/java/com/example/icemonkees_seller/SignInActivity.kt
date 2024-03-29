package com.example.icemonkees_seller

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.icemonkees_seller.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Autentication
        auth = FirebaseAuth.getInstance()

        // Set click listener for login button
        binding.btnLogin.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            // Authenticate user with Firebase
            signIn(username, password) // Pass username instead of email
        }
    }

    private fun signIn(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(
                        this, "Authentication successful.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Proceed to the next activity
                    startActivity(Intent(this, Dashboard::class.java))
                    finish() // Finish the current activity to prevent going back

                } else {
                    // If sign in fails, display a message to the user and clear input fields.
                    Toast.makeText(
                        baseContext, "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Clear input fields
                    binding.loginUsername.text = null
                    binding.loginPassword.text = null
                }
            }
    }
}
