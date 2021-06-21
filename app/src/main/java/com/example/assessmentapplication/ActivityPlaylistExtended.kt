package com.example.assessmentapplication

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_playlist_extended.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList
import kotlin.math.ceil

class ActivityPlaylistExtended : AppCompatActivity() {
    var list = listOf<Favourites>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_extended)

        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val playlistSize = sh.getInt("playlistSize", 0)

        val timesneeded = ceil(((playlistSize / 100).toDouble())) + 1

        for (i in 0 until timesneeded.toInt()) {
            val offset = ((i) * 100)
            dataload(offset)

        }


        backButton.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            startActivity(Intent(this, ActivityMain::class.java))
            true
        }
    }

    fun dataload(offset: Int) {
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val untrimmedurl = sh.getString("url", "").toString()
        val url = untrimmedurl.substring(untrimmedurl.lastIndexOf('/') + 1)
        val token = sh.getString("token", "").toString()
        val builder = SpotifyServiceBuilder.buildService(Services::class.java)
        val call = builder.getPlaylistSongs("Bearer $token", "$url", "$offset", "ES")
        call.enqueue(object : Callback<PlaylistTracks> {
            override fun onResponse(
                call: Call<PlaylistTracks>,
                response: Response<PlaylistTracks>
            ) {
                if (response.isSuccessful) {
                    val songs = response.body()!!
                    for(i in 0 until 100) {
                        try {
                            val toAdd = Favourites(
                                "${songs.items[i].track.name}",
                                "${songs.items[i].track.artists[0].name}"
                            )
                            list += toAdd
                        } catch (e: Exception) {
                            break
                        }
                    }
                    val recyclerView = findViewById<RecyclerView>(R.id.playlistSongsRecycler)
                    val adapter = PlaylistExtendedAdapter(list)
                    val layoutManager = LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter

                } else {
                    AlertDialog.Builder(this@ActivityPlaylistExtended)
                        .setTitle("API error")
                        .setMessage("Response, but something went wrong ${response.message()}. Please retry!")
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
                }
            }

            override fun onFailure(call: Call<PlaylistTracks>, t: Throwable) {
                AlertDialog.Builder(this@ActivityPlaylistExtended)
                    .setTitle("API error")
                    .setMessage("API call failed, $t")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        finish()
        overridePendingTransition(0, 0)
    }
}

