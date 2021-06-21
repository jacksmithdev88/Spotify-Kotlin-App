package com.example.assessmentapplication
import retrofit2.Call
import retrofit2.http.*

interface Services {
    @GET("v1/me/playlists")
    fun getPlaylist(@Header ("Authorization:") header: String): Call<Playlist>

    @GET("v1/me/")
    fun getUser(@Header ("Authorization:") header: String): Call<User>

    @GET("ra/songs.json")
    fun searchArtist(@Query("pattern") artist: String): Call<Tabs>

    @GET("v1/playlists/{playlist_id}/tracks")
    fun getPlaylistSongs(@Header("Authorization:") header: String, @Path("playlist_id") playlistID: String, @Query("offset") offset: String, @Query("market") market: String): Call<PlaylistTracks>
}