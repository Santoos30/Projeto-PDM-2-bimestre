package com.santoos30.spotifyapi2bi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import model.Atividade
import java.util.UUID

class PostAtvActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_post_atv)
        database = FirebaseDatabase.getInstance()

        val editTitulo     = findViewById<EditText>(R.id.TituloAtv)
        val editTipo       = findViewById<EditText>(R.id.MateriaAtv)
        val editDificuldade = findViewById<EditText>(R.id.DifAtv)
        val editResposta   = findViewById<EditText>(R.id.RespostaAtv)
        val btnSalvar      = findViewById<Button>(R.id.CriarAtv)
        val btnVoltar      = findViewById<ImageView>(R.id.btnVoltar)

        btnVoltar.setOnClickListener { finish() }

        btnSalvar.setOnClickListener {
            val titulo     = editTitulo.text.toString().trim()
            val tipo       = editTipo.text.toString().trim()
            val dificuldade = editDificuldade.text.toString().trim()
            val resposta   = editResposta.text.toString().trim()

            if (titulo.isEmpty() || tipo.isEmpty() || resposta.isEmpty()) {
                mostrarMensagem("Preencha todos os campos!")
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()
            val atividade = Atividade(
                id = id,
                titulo = titulo,
                tipo = tipo,
                dificuldade = dificuldade,
                respostaCorreta = resposta
            )
            database.getReference("atividades").child(id).setValue(atividade)
            mostrarMensagem("Atividade salva!")
            finish()
        }
    }
}