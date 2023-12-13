// BannedUserActivity.kt
package com.ecolink.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ecolink.R

class BannedUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banned_user_layout)

        // Set up click listener for the "Go Back" button
        val goBackButton: Button = findViewById(R.id.goBackButton)
        goBackButton.setOnClickListener {
            // Redirect to the landing page
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
