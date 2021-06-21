package com.example.assessmentapplication

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_your_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ActivityYourAccount : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_account)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val button = findViewById<Button>(R.id.changeTheme)
        val sharedPreferences = getSharedPreferences("SWITCH", MODE_PRIVATE)
        userLoad()
        val bottomnav: BottomNavigationView = findViewById(R.id.BottomNav)
        val dark = sharedPreferences.getBoolean("switched", false)
        val editor = sharedPreferences.edit()
        button.setOnClickListener {
            if (dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("switched", false).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("switched", true).apply()
            }
        }

        bottomnav.selectedItemId = R.id.YourAccount

        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Home -> {
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

        val sharebutton = findViewById<Button>(R.id.shareButton)
        sharebutton.setOnClickListener {
            val uri: Uri = Uri.parse("content://contacts")
            val intent = Intent(Intent.ACTION_PICK, uri)
            intent.type = Phone.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val cursor: Cursor?
        try {
            val uri = data?.data
            cursor = uri?.let { contentResolver.query(it, null, null, null, null, null) }
            if (cursor != null) {
                cursor.moveToFirst()
            }
            val phoneIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val phonenum = phoneIndex?.let { cursor.getString(it) }
            if (phonenum != null) {
                sendSMS(phonenum)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Message not sent", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        light?.let { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun sendSMS(phonenumber: String) {
        val smsManager = SmsManager.getDefault()
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val link = sh.getString("userURL", null)
        val sendnumber = phonenumber.replace("+44", "")
        smsManager.sendTextMessage("$sendnumber", null, "I am sharing my Spotify profile through SpotToTab. Here is the link: $link", null, null)
        Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show()
    }

    private fun userLoad() {
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sh.getString("token", "")
        val builder = SpotifyServiceBuilder.buildService(Services::class.java)
        val request = builder.getUser("Bearer $token")

        request.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()!!
                    val editor = sh.edit()

                    editor.putString("userURL", user.externalUrls.spotify).apply()
                    profileName.text = user.displayName
                    Picasso.get().load(user.images[0].url).into(profileImage)
                    followersText.text = "You have: ${user.followers.total} followers"
                    usernameText.text = "(" + user.id + ")"
                } else {
                    userLoad()
                    profileName.text = "Your profile could currently not be retrieved."
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userLoad()
                profileName.text = "Your profile could currently not be retrieved."
            }
        })
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val lux = event!!.values[0]
        if(lux < 800) {
            darkRecommend.text = "Low ambient light detected, dark mode is recommended"
        } else {
            darkRecommend.text = null
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}