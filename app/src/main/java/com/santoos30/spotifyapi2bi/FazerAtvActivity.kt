package com.santoos30.spotifyapi2bi

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.Normalizer
import androidx.appcompat.app.AlertDialog
import model.Atividade
import model.Crianca
import model.Resultado

class FazerAtvActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var spinnerCriancas: Spinner
    private lateinit var spinnerAtividades: Spinner
    private lateinit var edtResposta: EditText
    private lateinit var btnEnviar: Button
    private lateinit var btnVoltar: ImageView
    private val listaCriancas = mutableListOf<Crianca>()
    private val listaAtividades = mutableListOf<Atividade>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fzr_atv)

        database = FirebaseDatabase.getInstance()

        spinnerCriancas = findViewById(R.id.spinnerCriancas)
        spinnerAtividades = findViewById(R.id.spinnerAtividades)
        edtResposta = findViewById(R.id.edtResposta)
        btnEnviar = findViewById(R.id.btnEnviar)
        btnVoltar = findViewById(R.id.btnVoltar)

        carregarCriancas()
        carregarAtividades()

        btnEnviar.setOnClickListener {
            salvarResultado()
        }

        btnVoltar.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        }
    }

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

    private fun limparTexto(texto: String): String {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "").lowercase().trim()
    }

    private fun carregarCriancas() {
        database.getReference("criancas").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaCriancas.clear()
                    val nomes = mutableListOf<String>()
                    for (item in snapshot.children) {
                        val crianca = item.getValue(Crianca::class.java)
                        if (crianca != null) {
                            listaCriancas.add(crianca)
                            nomes.add(crianca.nome ?: "Sem nome")
                        }
                    }

                    val adapter = ArrayAdapter(this@FazerAtvActivity, R.layout.spinner_item, nomes)
                    spinnerCriancas.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun carregarAtividades() {
        database.getReference("atividades").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaAtividades.clear()
                    val titulos = mutableListOf<String>()
                    for (item in snapshot.children) {
                        val atividade = item.getValue(Atividade::class.java)
                        if (atividade != null) {
                            listaAtividades.add(atividade)
                            titulos.add("${atividade.tipo} - ${atividade.titulo}")
                        }
                    }
                    val adapter = ArrayAdapter(this@FazerAtvActivity, R.layout.spinner_item, titulos)
                    spinnerAtividades.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun salvarResultado() {
        if (listaCriancas.isEmpty() || listaAtividades.isEmpty()) {
            mostrarMensagem("Cadastre crianças e atividades primeiro!")
            return
        }
        val crianca = listaCriancas[spinnerCriancas.selectedItemPosition]
        val atividade = listaAtividades[spinnerAtividades.selectedItemPosition]
        val resposta = edtResposta.text.toString().trim()
        if (resposta.isEmpty()) {
            mostrarMensagem("Digite uma resposta!")
            return
        }
        val acertou = limparTexto(resposta) == limparTexto(atividade.respostaCorreta ?: "")
        val idResultado = database.getReference("resultados").push().key

        val resultado = Resultado(
            idResultado,
            crianca.id,
            crianca.nome,
            atividade.id,
            atividade.titulo,
            resposta,
            acertou
        )

        if (idResultado != null) {
            database.getReference("resultados").child(idResultado).setValue(resultado)

            if (acertou) {
                mostrarMensagem("Resposta correta!")
            } else {
                mostrarMensagem("Resposta incorreta.")
            }
            edtResposta.text.clear()
        }
    }
}