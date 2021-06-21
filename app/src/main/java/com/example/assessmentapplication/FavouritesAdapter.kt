package com.example.assessmentapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView

class FavouritesAdapter(private var dataSet: List<Favourites>) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songName: TextView = view.findViewById(R.id.songName)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val imageButton: ImageButton = view.findViewById(R.id.moreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.favourites_recycler_layout,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("NAME", dataSet.toString())
        holder.songName.text = "Song name: ${dataSet[position].name }"
        holder.artistName.text = "Artist name: ${dataSet[position].artist}"



        holder.imageButton.setOnClickListener{
            val popup = PopupMenu(it.context, it)
            popup.inflate(R.menu.favourites_popup)
            popup.show()
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId){
                    R.id.favourites -> {
                        val sharedPreferences = holder.itemView.context.getSharedPreferences(
                            "songs",
                            AppCompatActivity.MODE_PRIVATE
                        )
                        val editor = sharedPreferences.edit()
                        val current = sharedPreferences.getString("songs", null)

                        val list = current?.split("+")?.toMutableList()
                        if (list != null) {
                            if (list.size == 1) {
                                editor.clear().commit()
                            } else {
                                Log.d("UNDROPPED", list.toString())
                                list?.removeAt(position)
                                Log.d("DROPPED", list.toString())

                                var newList = String()
                                Log.d("SIZE", list?.size.toString())
                                for (i in 0 until list.size - 1) {
                                    newList += "${list?.get(i)}+"
                                }
                                newList += "${list?.get(list.size - 1)}"
                                if (newList == null) {
                                    editor.clear().commit()
                                } else {
                                    editor.putString("songs", newList).apply()
                                }
                            }
                        }
                        Toast.makeText(it.context, "Removed from favourites", Toast.LENGTH_SHORT)
                            .show()

                        (holder.itemView.context as Activity).finish()
                        (holder.itemView.context as Activity).overridePendingTransition(0,0)
                        holder.itemView.context.startActivity(Intent(holder.itemView.context, ActivityFavourites::class.java))

                        true
                    }
                    R.id.browserView -> {
                        val context = holder.itemView.context
                        val intent =
                            Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.songsterr.com/a/wa/bestMatchForQueryString?s=${dataSet[position].name}&a=${dataSet[position].artist}"))
                        context.startActivity(intent)
                        true
                    }
                    else -> false
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }
}
