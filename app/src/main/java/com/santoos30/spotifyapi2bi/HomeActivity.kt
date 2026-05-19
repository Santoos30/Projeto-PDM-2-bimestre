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
        val BtnTela4 = findViewById<ImageView>(R.id.PostCriancas)
        val BtnTela3 = findViewById<ImageView>(R.id.GetCriancas)
        val BtnTela2 = findViewById<ImageView>(R.id.PostAtividades)
        val BtnTela1 = findViewById<ImageView>(R.id.GetAtividades)
        val BtnTela0 = findViewById<ImageView>(R.id.btnSpotify)

        BtnTela4.setOnClickListener {
            val Tela4 = Intent(this, PostCrianActivity::class.java)
            startActivity (Tela4)
        }

        BtnTela3.setOnClickListener {
            val Tela3 = Intent(this, GetCrianActivity::class.java)
            startActivity (Tela3)
        }

        BtnTela2.setOnClickListener {
            val Tela2 = Intent(this, PostAtvActivity::class.java)
            startActivity (Tela2)
        }

        BtnTela1.setOnClickListener {
            val Tela1 = Intent(this, GetAtvActivity::class.java)
            startActivity (Tela1)
        }

        BtnTela0.setOnClickListener {
            val Tela0 = Intent(this, SpotifyActivity::class.java)
            startActivity (Tela0)
        }
    }
}