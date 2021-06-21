package com.example.assessmentapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_tab.*

class ActivityFavourites : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        val clearbutton = findViewById<Button>(R.id.clearButton)
        clearbutton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("songs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            Toast.makeText(this@ActivityFavourites, "Favourites cleared", Toast.LENGTH_SHORT).show()
            editor.clear().commit()
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        val bottomnav: BottomNavigationView = findViewById(R.id.BottomNav)
        bottomnav.selectedItemId = R.id.Tabs
        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Home -> {
                    finish()
                    startActivity(Intent(this, ActivityMain::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.YourAccount -> {
                    finish()
                    startActivity(Intent(this, ActivityYourAccount::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    true
                }
                R.id.Search -> {
                    finish()
                    startActivity(Intent(this, ActivitySearch::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.Tabs -> {
                    startActivity(Intent(this, ActivityFavourites::class.java))
                    true
                }
                else -> false
            }
        }
        loadData()
    }

    fun loadData() {
        val sharedPreferences = getSharedPreferences("songs", MODE_PRIVATE)
        val current = sharedPreferences.getString("songs", null)
        val list = current?.split("+")
        var favList = listOf<Favourites>()
        if (list != null) {
            for (i in list.indices){
                val current = list.get(i)
                val split = current.split(",")
                val toAdd = Favourites("${split[0]}", "${split[1]}")
                favList += toAdd
            }
        }
        val countext = findViewById<TextView>(R.id.countText)
        countext.text = "Number of favourites: ${favList.size}"
        val recyclerView = findViewById<RecyclerView>(R.id.favouritesRecycler)
        val adapter = FavouritesAdapter(favList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}

