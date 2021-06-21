package com.example.assessmentapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PlaylistAdapter(private val dataSet: Playlist) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistName: TextView = view.findViewById(R.id.playlistName)
        val songCount: TextView = view.findViewById(R.id.songcount)
        val playlistImage: ImageView = view.findViewById(R.id.playlistImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.playlistName.text = dataSet.items[position].name
        holder.songCount.text = "Song count: ${ dataSet.items[position].tracks.total.toString()}"
        Picasso.get().load(dataSet.items[position].images[0].url).into(holder.playlistImage)

        holder.itemView.setOnClickListener {
            val sharedPreferences = holder.itemView.context.getSharedPreferences("MySharedPref",
                AppCompatActivity.MODE_PRIVATE
            )
            val myEdit = sharedPreferences.edit()
            myEdit.putString("url", dataSet.items[position].href)
            myEdit.putInt("playlistSize", dataSet.items[position].tracks.total)
            myEdit.apply()

            holder.itemView.context.startActivity(Intent(holder.itemView.context, ActivityPlaylistExtended::class.java))
        }
    }

    override fun getItemCount() = dataSet.items.size


}