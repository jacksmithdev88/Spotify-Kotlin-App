package com.example.assessmentapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_your_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitySearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val bottomnav: BottomNavigationView = findViewById(R.id.BottomNav)
        bottomnav.selectedItemId = R.id.Search
        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener{
            tabSearch()

        }

        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Home -> {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    startActivity(Intent(this, ActivityMain::class.java))
                    true
                }
                R.id.YourAccount -> {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    startActivity(Intent(this, ActivityYourAccount::class.java))
                    true
                }
                R.id.Search -> {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    startActivity(Intent(this, ActivitySearch::class.java))
                    true
                }
                R.id.Tabs -> {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    startActivity(Intent(this, ActivityFavourites::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }


    private fun tabSearch() {
        val artistText: EditText = findViewById(R.id.artistName)
        val songName: EditText = findViewById(R.id.songName)
         val nsartist = artistText.text.toString().replace(" ", "+")
         val nssongName = songName.text.toString().replace(" ", "+")

        if(nsartist != "" && nssongName != ""){
            val builder = SongsterServiceBuilder.buildService(Services::class.java)
            val call = builder.searchArtist("$nsartist + $nssongName")

            call.enqueue(object : Callback<Tabs> {
                override fun onResponse(
                    call: Call<Tabs>,
                    response: Response<Tabs>
                ) {
                    if (response.isSuccessful) {
                        val tabs = response.body()!!
                        if (tabs.size == 0) {
                            Toast.makeText(this@ActivitySearch, "No results found", Toast.LENGTH_SHORT).show()
                        } else {
                            val tabs = response.body()!!
                            val recyclerView = findViewById<RecyclerView>(R.id.searchRecycle)
                            val adapter = SearchAdapter(tabs)
                            val layoutManager = LinearLayoutManager(applicationContext)
                            recyclerView.layoutManager = layoutManager
                            recyclerView.adapter = adapter
                        }
                    } else {
                        AlertDialog.Builder(this@ActivitySearch)
                            .setTitle("API error")
                            .setMessage("Response, but something went wrong ${response.message()}")
                            .setPositiveButton(android.R.string.ok) { _, _ -> }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                    }
                }
                override fun onFailure(call: Call<Tabs>, t: Throwable) {
                    invalidText.text = "We could not retrieve any results. Please check your connection"
                }
            })
        }

         if(nsartist != "" && nssongName == "") {
             val builder = SongsterServiceBuilder.buildService(Services::class.java)
             val call = builder.searchArtist(nsartist)

             call.enqueue(object : Callback<Tabs> {
                 override fun onResponse(
                     call: Call<Tabs>,
                     response: Response<Tabs>
                 ) {
                     if (response.isSuccessful) {
                         val tabs = response.body()!!
                         if (tabs.size == 0) {
                             Toast.makeText(this@ActivitySearch, "No results found", Toast.LENGTH_SHORT).show()
                         } else {
                             val tabs = response.body()!!
                             val recyclerView = findViewById<RecyclerView>(R.id.searchRecycle)
                             val adapter = SearchAdapter(tabs)
                             val layoutManager = LinearLayoutManager(applicationContext)
                             recyclerView.layoutManager = layoutManager
                             recyclerView.adapter = adapter
                         }
                     } else {
                         AlertDialog.Builder(this@ActivitySearch)
                             .setTitle("API error")
                             .setMessage("Response, but something went wrong ${response.message()}")
                             .setPositiveButton(android.R.string.ok) { _, _ -> }
                             .setIcon(android.R.drawable.ic_dialog_alert)
                             .show()
                     }
                 }
                 override fun onFailure(call: Call<Tabs>, t: Throwable) {
                     invalidText.text = "We could not retrieve any results. Please check your connection"
                 }
             })
         }

         if(nsartist == "" && nssongName != "") {
             val builder = SongsterServiceBuilder.buildService(Services::class.java)
             val call = builder.searchArtist(nssongName)

             call.enqueue(object : Callback<Tabs> {
                 override fun onResponse(
                     call: Call<Tabs>,
                     response: Response<Tabs>
                 ) {
                     if (response.isSuccessful) {
                         val tabs = response.body()!!

                         if (tabs.size == 0) {
                             Toast.makeText(this@ActivitySearch, "No results found", Toast.LENGTH_SHORT).show()
                         } else {
                             val recyclerView = findViewById<RecyclerView>(R.id.searchRecycle)
                             val adapter = SearchAdapter(tabs)
                             val layoutManager = LinearLayoutManager(applicationContext)
                             recyclerView.layoutManager = layoutManager
                             recyclerView.adapter = adapter
                         }
                     } else {
                         AlertDialog.Builder(this@ActivitySearch)
                             .setTitle("API error")
                             .setMessage("Response, but something went wrong ${response.message()}")
                             .setPositiveButton(android.R.string.ok) { _, _ -> }
                             .setIcon(android.R.drawable.ic_dialog_alert)
                             .show()
                     }
                 }

                 override fun onFailure(call: Call<Tabs>, t: Throwable) {
                     invalidText.text = "We could not retrieve any results. Please check your connection"
                 }
             })
         }

         if (artistText == null && songName == null) {
             Toast.makeText(this@ActivitySearch ,"Please input data into one of the fields", Toast.LENGTH_LONG).show()
         }
    }
}