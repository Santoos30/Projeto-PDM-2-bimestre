package com.santoos30.spotifyapi2bi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import model.Atividade
import java.util.UUID

class PostAtvActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_atv)
        database = FirebaseDatabase.getInstance()
        val editTitulo =
            findViewById<EditText>(R.id.TituloAtv)
        val editTipo =
            findViewById<EditText>(R.id.TipoAtv)
        val editDificuldade =
            findViewById<EditText>(R.id.DifAtv)
        val btnSalvar =
            findViewById<Button>(R.id.CriarAtv)
        btnSalvar.setOnClickListener {
            val id = UUID.randomUUID().toString()
            val atividade = Atividade(
                id,
                editTitulo.text.toString(),
                editTipo.text.toString(),
                editDificuldade.text.toString()
            )
            database
                .getReference("atividades")
                .child(id)
                .setValue(atividade)
            Toast.makeText(this, "Atividade salva!",
                Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}