package com.ecolink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.ecolink.brand.Addproduit
import com.ecolink.databinding.ActivityMainBinding
import com.ecolink.fragments.EventsFragment
import com.ecolink.fragments.NewsFragment
import com.ecolink.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar.appBar
        setSupportActionBar(toolbar)

        binding.btnNews.setOnClickListener {
            changeFragment(NewsFragment(), "")
            binding.addProjet.visibility = View.VISIBLE

        }
        binding.addProjet.setOnClickListener {
            changeFragment(Addproduit(), "")

            // Hide the button after navigation
            binding.addProjet.visibility = View.GONE
        }



        binding.btnEvents.setOnClickListener {
            changeFragment(EventsFragment(), "")
        }

        binding.btnProfile.setOnClickListener {
            changeFragment(ProfileFragment(), "")
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NewsFragment()).commit()
    }

    private fun changeFragment(fragment: Fragment, name: String) {

        if (name.isEmpty())
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        else
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("").commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.infoMenu -> {
                // Handle the click event for "Lesson" menu item
                // For example, you can start a new activity or perform some other action
                val intent = Intent(this, MainActivity22::class.java)
                startActivity(intent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}