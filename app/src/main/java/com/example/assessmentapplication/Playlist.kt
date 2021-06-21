package com.example.assessmentapplication


import com.google.gson.annotations.SerializedName

data class Playlist(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
) {
    data class Item(
        val collaborative: Boolean,
        val description: String,
        @SerializedName("external_urls")
        val externalUrls: ExternalUrls,
        val href: String,
        val id: String,
        val images: List<Image>,
        val name: String,
        val owner: Owner,
        @SerializedName("primary_color")
        val primaryColor: Any,
        val `public`: Boolean,
        @SerializedName("snapshot_id")
        val snapshotId: String,
        val tracks: Tracks,
        val type: String,
        val uri: String
    ) {
        data class ExternalUrls(
            val spotify: String
        )

        data class Image(
            val height: Int,
            val url: String,
            val width: Int
        )

        data class Owner(
            @SerializedName("display_name")
            val displayName: String,
            @SerializedName("external_urls")
            val externalUrls: ExternalUrls,
            val href: String,
            val id: String,
            val type: String,
            val uri: String
        ) {
            data class ExternalUrls(
                val spotify: String
            )
        }

        data class Tracks(
            val href: String,
            val total: Int
        )
    }
}