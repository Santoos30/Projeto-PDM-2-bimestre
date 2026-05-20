package com.santoos30.spotifyapi2bi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import model.Crianca
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PostCrianActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_crian)
        val btnSalvar = findViewById<Button>(R.id.BotaoSalvar)
        val inputNome = findViewById<EditText>(R.id.NomeCrianca)
        val inputIdd = findViewById<EditText>(R.id.IdadeCrianca)
        val inputLeitura = findViewById<EditText>(R.id.NivelLeitura)
        val inputMath = findViewById<EditText>(R.id.NivelMatematica)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        database = FirebaseDatabase.getInstance()

        btnSalvar.setOnClickListener {
            val nome = inputNome.text.toString()
            val idade = inputIdd.text.toString().toInt()
            val nivelLeitura = inputLeitura.text.toString()
            val nivelMatematica = inputMath.text.toString()
            val id = database.getReference("criancas").push().key
            val crianca = Crianca(
                id,
                nome,
                idade,
                nivelLeitura,
                nivelMatematica
            )
            if (id != null) {
                database
                    .getReference("criancas")
                    .child(id)
                    .setValue(crianca)
                Toast.makeText(this, "Criança cadastrada!", Toast.LENGTH_SHORT).show()
            }
        }

        btnVoltar.setOnClickListener{
            finish ()
        }
    }
}