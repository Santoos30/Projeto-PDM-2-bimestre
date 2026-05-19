package com.santoos30.spotifyapi2bi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import spotify.RetrofitInstance
import spotify.SpotifyResponse

class SpotifyActivity : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private var spotifyUrl: String? = null

    private lateinit var txtNome: TextView
    private lateinit var txtArtista: TextView
    private lateinit var imgAlbum: ImageView
    private lateinit var editPesquisa: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

        txtNome = findViewById(R.id.txtNome)
        txtArtista = findViewById(R.id.txtArtista)
        imgAlbum = findViewById(R.id.imgAlbum)
        editPesquisa = findViewById(R.id.editPesquisa)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnVoltar = findViewById(R.id.btnVoltar)
        btnPlay = findViewById(R.id.btnPlay)

        // Botão começa desabilitado até buscar uma música
        btnPlay.isEnabled = false
        btnPlay.text = "Abrir no Spotify"

        btnVoltar.setOnClickListener {
            finish()
        }

        btnBuscar.setOnClickListener {
            val musica = editPesquisa.text.toString()

            if (musica.isEmpty()) {
                Toast.makeText(this, "Digite uma música!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SpotifyAuth.getToken { token ->
                if (token != null) {
                    buscarMusica(token, musica)
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Erro ao gerar token!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Abre o link no app do Spotify (ou no navegador se não tiver instalado)
        btnPlay.setOnClickListener {
            if (spotifyUrl != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUrl))
                intent.setPackage("com.spotify.music") // tenta abrir no app

                // Se o app do Spotify não estiver instalado, abre no navegador
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUrl)))
                }
            } else {
                Toast.makeText(this, "Busque uma música primeiro!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarMusica(token: String, musica: String) {
        RetrofitInstance.api.buscarMusica(
            "Bearer $token",
            musica
        ).enqueue(object : Callback<SpotifyResponse> {

            override fun onResponse(
                call: Call<SpotifyResponse>,
                response: Response<SpotifyResponse>
            ) {
                if (response.isSuccessful) {
                    val track = response.body()?.tracks?.items?.getOrNull(0)

                    runOnUiThread {
                        if (track == null) {
                            Toast.makeText(
                                this@SpotifyActivity,
                                "Nenhuma música encontrada!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@runOnUiThread
                        }

                        txtNome.text = track.name
                        txtArtista.text = track.artists[0].name

                        Glide.with(this@SpotifyActivity)
                            .load(track.album.images[0].url)
                            .into(imgAlbum)

                        spotifyUrl = track.external_urls.spotify

                        btnPlay.isEnabled = true
                        btnPlay.text = "Abrir no Spotify"
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@SpotifyActivity,
                            "Música não encontrada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<SpotifyResponse>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(
                        this@SpotifyActivity,
                        "Erro: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}