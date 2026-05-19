package com.santoos30.spotifyapi2bi

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.util.Base64

object SpotifyAuth {

    private const val CLIENT_ID = "15cc6dc423334913a1a4221e7a6be72c"
    private const val CLIENT_SECRET = "a2e9c046f1ad4681b10d0a4524a3b382"

    fun getToken(callback: (String?) -> Unit) {
        val client = OkHttpClient()
        val auth = Base64.encodeToString(
            "$CLIENT_ID:$CLIENT_SECRET".toByteArray(),
            Base64.NO_WRAP
        )
        val body = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()
        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(body)
            .addHeader("Authorization", "Basic $auth")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }
            override fun onResponse(call: Call, response: Response) {
                val json =
                    JSONObject(response.body!!.string())
                val token =
                    json.getString("access_token")
                callback(token)
            }
        })
    }
}