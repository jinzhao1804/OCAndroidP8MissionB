package com.example.p8vitesse

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.p8vitesse.data.database.AppDatabase
import com.example.p8vitesse.ui.home.ViewPagerAdapter
import com.example.p8vitesse.ui.home.all.AllFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // AppDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager)
        val searchView = findViewById<EditText>(R.id.search_bar_edit_text)
        val searchButton = findViewById<ImageView>(R.id.search_bar_hint_icon)

        // Set up ViewPager with the adapter
        viewPager.adapter = ViewPagerAdapter(this)

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tous"
                1 -> "Favoris"
                else -> null
            }
        }.attach()

        // Listen for text changes in the search EditText
        searchView.addTextChangedListener { text ->
            val query = text.toString()
            // Pass the query to the AllFragment
            val fragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
            if (fragment is AllFragment) {
                fragment.filterCandidates(query)  // Update the fragment with the filtered query
            }
        }

        //when click on button search
        searchButton.setOnClickListener {
            // Get the query from the EditText
            val query = searchView.text.toString()  // Get the text entered in the search EditText

            // Find the currently displayed fragment based on ViewPager's current item
            val fragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
            if (fragment is AllFragment) {
                // Pass the query to the AllFragment to filter the candidates
                fragment.filterCandidates(query)
            }
        }

    }
}
