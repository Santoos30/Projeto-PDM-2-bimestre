package com.santoos30.spotifyapi2bi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val BtnTela1 = findViewById<ImageView>(R.id.PostCriancas)
        val BtnTela2 = findViewById<ImageView>(R.id.GetCriancas)
        val BtnTela3 = findViewById<ImageView>(R.id.PostAtividades)
        val BtnTela4 = findViewById<ImageView>(R.id.FzrAtividades)
        val BtnTela5 = findViewById<ImageView>(R.id.GetAtividades)
        val BtnTela6 = findViewById<ImageView>(R.id.Historico)
        val BtnTela7 = findViewById<ImageView>(R.id.btnSpotify)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)

        btnVoltar.setOnClickListener{
            val Login = Intent(this, LoginActivity::class.java)
            startActivity(Login)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela1.setOnClickListener {
            val Tela1 = Intent(this, PostCrianActivity::class.java)
            startActivity(Tela1)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela2.setOnClickListener {
            val Tela2 = Intent(this, GetCrianActivity::class.java)
            startActivity (Tela2)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela3.setOnClickListener {
            val Tela3 = Intent(this, PostAtvActivity::class.java)
            startActivity (Tela3)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela4.setOnClickListener {
            val Tela4 = Intent(this, FazerAtvActivity::class.java)
            startActivity (Tela4)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela5.setOnClickListener {
            val Tela5 = Intent(this, GetAtvActivity::class.java)
            startActivity (Tela5)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela6.setOnClickListener {
            val Tela6 = Intent(this, HistoricoActivity::class.java)
            startActivity (Tela6)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }

        BtnTela7.setOnClickListener {
            val Tela7 = Intent(this, SpotifyActivity::class.java)
            startActivity(Tela7)
            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }
    }
}