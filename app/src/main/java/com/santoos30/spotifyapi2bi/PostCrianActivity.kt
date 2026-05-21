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
import androidx.appcompat.app.AlertDialog

class PostCrianActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

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
            val nome = inputNome.text.toString().trim()
            val idade = inputIdd.text.toString().toIntOrNull()
            val nivelLeitura = inputLeitura.text.toString().trim()
            val nivelMatematica = inputMath.text.toString().trim()

            if (nome.isEmpty()) {
                mostrarMensagem("Digite o nome!")
                return@setOnClickListener
            }
            if (idade == null) {
                mostrarMensagem("Digite uma idade válida!")
                return@setOnClickListener
            }
            if (nivelLeitura.isEmpty()) {
                mostrarMensagem("Digite o nível de leitura!")
                return@setOnClickListener
            }
            if (nivelMatematica.isEmpty()) {
                mostrarMensagem("Digite o nível de matemática!")
                return@setOnClickListener
            }

            val id = database.getReference("criancas").push().key
            val crianca = Crianca(
                id,
                nome,
                idade,
                nivelLeitura,
                nivelMatematica
            )

            if (id != null) {
                database.getReference("criancas").child(id).setValue(crianca)
                    .addOnSuccessListener {
                        mostrarMensagem("Criança cadastrada!")
                    }.addOnFailureListener {
                        mostrarMensagem("Erro ao cadastrar!")
                    }
            }
        }
        btnVoltar.setOnClickListener {
            finish()
        }
    }
}