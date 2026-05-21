package spotify

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApi {
    @GET("search")
    fun buscarMusica(
        @Header("Authorization")
        token: String,
        @Query("q")
        musica: String,
        @Query("type")
        type: String = "track",
        @Query("limit")
        limit: Int = 10
    ): Call<SpotifyResponse>
}