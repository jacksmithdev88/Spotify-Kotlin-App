package com.example.assessmentapplication

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

class SearchAdapter(private val dataSet: Tabs) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songName: TextView = view.findViewById(R.id.searched_song_name)
        val artistName: TextView = view.findViewById(R.id.searched_artist_name)
        val imageButton: ImageButton = view.findViewById(R.id.moreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.songName.text = "Song name: ${dataSet[position].title}"
        holder.artistName.text = "Artist name: ${dataSet[position].artist.name}"

        holder.imageButton.setOnClickListener {
            val popup = PopupMenu(it.context, it)
            popup.inflate(R.menu.search_popup)
            popup.show()
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.favourites -> {
                        val sharedPreferences =
                            holder.itemView.context.getSharedPreferences("songs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val current = sharedPreferences.getString("songs", null)
                        val currentList = current?.split("+")
                        val currentAmount = currentList?.size
                        var duplicate = false
                        if (currentAmount != null) {
                            for (i in 0 until currentAmount!!) {

                                val items = currentList[i].split(",")

                                if (items[0] == dataSet[position].title && items[1] == dataSet[position].artist.name) {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Song is already in favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    duplicate = true
                                    break
                                }
                            }
                        }
                        if(!duplicate) {
                            Log.d("DUPLICATE NO ", "NO")
                            editor.putString("songs", "$current+${dataSet[position].title},${dataSet[position].artist.name}").apply()
                            Toast.makeText(it.context, "Added to favourites", Toast.LENGTH_SHORT).show()
                        }
                        if(currentAmount == null) {
                            editor.putString(
                                "songs",
                                "${dataSet[position].title},${dataSet[position].artist.name}"
                            ).apply()
                            Toast.makeText(it.context, "Added to favourites", Toast.LENGTH_SHORT)
                                .show()
                        }
                        true
                    }
                    R.id.browserView -> {
                        val context = holder.itemView.context
                        val intent =
                            Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.songsterr.com/a/wa/bestMatchForQueryString?s=${dataSet[position].title}&a=${dataSet[position].artist.name}"))
                        context.startActivity(intent)
                        true
                    }
                    else -> false
                }

            }
        }
    }
    override fun getItemCount() = dataSet.size
}
