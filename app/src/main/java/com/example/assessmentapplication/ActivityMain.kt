package com.example.assessmentapplication


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomnav: BottomNavigationView = findViewById(R.id.BottomNav)
        bottomnav.selectedItemId = R.id.Home
        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Home -> {
                    true
                }
                R.id.YourAccount -> {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    startActivity(Intent(this, ActivityYourAccount::class.java))
                    true
                }
                R.id.Search -> {
                    finish()
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

    override fun onStart() {
        super.onStart()
        authenticateSpotify()
        dataload()
    }

    private fun authenticateSpotify() {
        val redirect = "com.example.assessmentapplication://callback"
        val clientID = "2b4f6de8ba0a413db643fe8f4eb00835"
        val requestCode = 1337
        val auth =
            AuthenticationRequest.Builder(clientID, AuthenticationResponse.Type.TOKEN, redirect)
        auth.setScopes(arrayOf("playlist-read-private"))
        val request = auth.build()
        AuthenticationClient.openLoginActivity(this, requestCode, request)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val token = AuthenticationClient.getResponse(resultCode, intent).accessToken

        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("token", token)
        myEdit.commit()
    }

    private fun dataload() {
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sh.getString("token", "")
        val builder = SpotifyServiceBuilder.buildService(Services::class.java)
        val request = builder.getPlaylist("Bearer $token")

        request.enqueue(object : Callback<Playlist> {
            override fun onResponse(
                call: Call<Playlist>,
                response: Response<Playlist>
            ) {
                
                if (response.isSuccessful) {
                    val playlist = response.body()!!

                    val recyclerView: RecyclerView = findViewById(R.id.playlistR)
                    val adapter = PlaylistAdapter(playlist)
                    val layoutManager = LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter


                } else {
                    dataload()
                }
            }
            override fun onFailure(call: Call<Playlist>, t: Throwable){
                invalidText.text = "Could not retrieve playlists. Please check your connection or try again!"
            }
        })
    }

}
