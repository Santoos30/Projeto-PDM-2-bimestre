package com.santoos30.spotifyapi2bi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import spotify.RetrofitInstance
import androidx.appcompat.app.AlertDialog
import spotify.SpotifyResponse

class SpotifyActivity : AppCompatActivity() {

    private lateinit var btnPlay: ImageView
    private var spotifyUrl: String? = null

    private lateinit var txtNome: TextView
    private lateinit var txtArtista: TextView
    private lateinit var imgAlbum: ImageView
    private lateinit var editPesquisa: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnVoltar: ImageView

    private fun mostrarMensagem(mensagem: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_mensagem, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val txtMensagem = dialogView.findViewById<TextView>(R.id.txtMensagemDialog)
        val btnOk = dialogView.findViewById<Button>(R.id.btnOkDialog)

        txtMensagem.text = mensagem
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
    }

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

        btnPlay.isEnabled = false

        btnVoltar.setOnClickListener {
            finish()
        }

        btnBuscar.setOnClickListener {
            val musica = editPesquisa.text.toString()

            if (musica.isEmpty()) {
                mostrarMensagem("Digite uma música!")
                return@setOnClickListener
            }

            SpotifyAuth.getToken { token ->
                if (token != null) {
                    buscarMusica(token, musica)
                } else {
                    runOnUiThread {
                        mostrarMensagem("Erro ao gerar token!")
                    }
                }
            }
        }

        btnPlay.setOnClickListener {
            if (spotifyUrl != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUrl))
                intent.setPackage("com.spotify.music")

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUrl)))
                }
            } else {
                mostrarMensagem("Busque uma música primeiro!")
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
                           mostrarMensagem("Nenhuma música encontrada!",)
                            return@runOnUiThread
                        }

                        txtNome.text = track.name
                        txtArtista.text = track.artists[0].name

                        Glide.with(this@SpotifyActivity)
                            .load(track.album.images[0].url)
                            .into(imgAlbum)

                        spotifyUrl = track.external_urls.spotify

                        btnPlay.isEnabled = true
                    }
                } else {
                    runOnUiThread {
                        mostrarMensagem("Música não encontrada!")
                    }
                }
            }

            override fun onFailure(call: Call<SpotifyResponse>, t: Throwable) {
                runOnUiThread {
                    mostrarMensagem("Erro: $t")
                }
            }
        })
    }
}