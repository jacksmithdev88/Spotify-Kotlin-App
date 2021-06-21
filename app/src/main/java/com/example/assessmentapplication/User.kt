package com.example.assessmentapplication


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val type: String,
    val uri: String
) {
    data class ExternalUrls(
        val spotify: String
    )

    data class Followers(
        val href: Any,
        val total: Int
    )

    data class Image(
        val height: Any,
        val url: String,
        val width: Any
    )
}