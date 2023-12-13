package com.ecolink

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ecolink.ui.LandingPageActivity
// SplashActivity.kt
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val splashLogo = findViewById<ImageView>(R.id.splashLogo)

        // Create a scale animation from 50dp to 300dp
        val scaleAnimation = ScaleAnimation(
            1f, // From X scale factor
            6f, // To X scale factor (300dp / 50dp)
            1f, // From Y scale factor
            6f, // To Y scale factor (300dp / 50dp)
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point (X) - center
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point (Y) - center
        )

        scaleAnimation.duration = 5000 // Set the animation duration in milliseconds
        scaleAnimation.interpolator = OvershootInterpolator() // Add an interpolator if needed

        // Set an animation listener to start a new activity when the animation ends
        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Animation started
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Animation ended, start a new activity or perform other actions
                startNextActivity()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated (if set)
            }
        })

        // Start the animation
        splashLogo.startAnimation(scaleAnimation)
    }

    private fun startNextActivity() {
        // Add code to start the next activity (e.g., MainActivity)
        val intent = Intent(this, LandingPageActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to the splash screen
    }
}
