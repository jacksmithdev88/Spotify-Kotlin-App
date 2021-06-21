package com.example.assessmentapplication


import com.google.gson.annotations.SerializedName

class Tabs : ArrayList<Tabs.TabsItem>(){
    data class TabsItem(
        val artist: Artist,
        val title: String,
    ) {
        data class Artist(
            val name: String,
        )
    }
}