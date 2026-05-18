package com.santoos30.spotifyapi2bi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val BtnTela4 = findViewById<Button>(R.id.CriarCrian)
        val BtnTela3 = findViewById<Button>(R.id.ListarCrian)
        val BtnTela2 = findViewById<Button>(R.id.CriarAtv)
        val BtnTela1 = findViewById<Button>(R.id.ListarAtv)

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
    }
}