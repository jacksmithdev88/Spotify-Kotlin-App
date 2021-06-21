package com.example.assessmentapplication

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
data class Favourites (
    val name: String?,
    val artist: String?)