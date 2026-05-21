package com.santoos30.spotifyapi2bi

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.*
import model.Atividade

class GetAtvActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var tabelaAtividades: TableLayout

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
        setContentView(R.layout.activity_get_atv)
        database = FirebaseDatabase.getInstance()
        tabelaAtividades = findViewById(R.id.tabelaAtividades)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)

        database.getReference("atividades").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (tabelaAtividades.childCount > 1) {
                        tabelaAtividades.removeViews(1, tabelaAtividades.childCount - 1)
                    }

                    for (item in snapshot.children) {
                        val atividade = item.getValue(Atividade::class.java)
                        val row = TableRow(this@GetAtvActivity)

                        fun criarTextView(texto: String): TextView {
                            return TextView(this@GetAtvActivity).apply {
                                text = texto
                                setPadding(12, 8, 12, 8)
                                textSize = 18f
                                typeface = ResourcesCompat.getFont(context, R.font.allerta)
                                paint.isFakeBoldText = true
                                setTextColor(Color.parseColor("#00A88F"))
                                setShadowLayer(7f, 0f, 0f, Color.parseColor("#004d41"))
                                gravity = Gravity.CENTER
                                setSingleLine(true)
                            }
                        }

                        val txtId = criarTextView(atividade?.id
                                ?.replace("-", "")
                                ?.take(5) ?: ""
                        )

                        val txtTitulo = criarTextView(
                            atividade?.titulo ?: ""
                        )

                        val txtTipo = criarTextView(
                            atividade?.tipo ?: ""
                        )

                        val txtDificuldade = criarTextView(
                            atividade?.dificuldade ?: ""
                        )

                        val txtResposta = criarTextView(
                            atividade?.respostaCorreta ?: ""
                        )

                        val btnExcluir = TextView(this@GetAtvActivity)
                        btnExcluir.text = "X"
                        btnExcluir.textSize = 20f
                        btnExcluir.setTextColor(Color.parseColor("#FF4C4C"))
                        btnExcluir.setPadding(12, 8, 12, 8)

                        btnExcluir.setOnClickListener {
                            atividade?.id?.let {
                                    id -> database.getReference("atividades").child(id).removeValue()
                                mostrarMensagem("Atividade removida!")
                            }
                        }

                        val btnEditar = TextView(this@GetAtvActivity)
                        btnEditar.text = "✐"
                        btnEditar.textSize = 20f
                        btnEditar.setTextColor(Color.parseColor("#FFC107"))
                        btnEditar.setPadding(12, 8, 12, 8)

                        btnEditar.setOnClickListener {
                            val dialogView = layoutInflater.inflate(R.layout.dialog_editar_atv, null)
                            val dialog = AlertDialog.Builder(this@GetAtvActivity).setView(dialogView).create()
                            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                            dialog.show()

                            val btnSalvar = dialogView.findViewById<TextView>(R.id.btnSalvarDialog)
                            val btnCancelar = dialogView.findViewById<TextView>(R.id.btnCancelarDialog)
                            val editTitulo = dialogView.findViewById<EditText>(R.id.editTituloDialog)
                            val editTipo = dialogView.findViewById<EditText>(R.id.editTipoDialog)
                            val editDificuldade = dialogView.findViewById<EditText>(R.id.editDificuldadeDialog)
                            val editResposta = dialogView.findViewById<EditText>(R.id.editRespostaDialog)

                            editTitulo.setText(atividade?.titulo)
                            editTipo.setText(atividade?.tipo)
                            editDificuldade.setText(atividade?.dificuldade)
                            editResposta.setText(atividade?.respostaCorreta)

                            btnCancelar.setOnClickListener {
                                dialog.dismiss()
                            }

                            btnSalvar.setOnClickListener {
                                val titulo = editTitulo.text.toString().trim()
                                val tipo = editTipo.text.toString().trim()
                                val dificuldade = editDificuldade.text.toString().trim()
                                val resposta = editResposta.text.toString().trim()
                                if (titulo.isEmpty() || tipo.isEmpty() || dificuldade.isEmpty() || resposta.isEmpty()) {
                                    mostrarMensagem("Preencha todos os campos!")
                                    return@setOnClickListener
                                }
                                val novosDados = mapOf(
                                    "titulo" to titulo,
                                    "tipo" to tipo,
                                    "dificuldade" to dificuldade,
                                    "respostaCorreta" to resposta
                                )

                                atividade?.id?.let {
                                        id ->database.getReference("atividades").child(id).updateChildren(novosDados)
                                    mostrarMensagem("Atividade atualizada!")
                                }
                                dialog.dismiss()
                            }
                        }

                        row.addView(txtId)
                        row.addView(txtTitulo)
                        row.addView(txtTipo)
                        row.addView(txtDificuldade)
                        row.addView(txtResposta)
                        row.addView(btnEditar)
                        row.addView(btnExcluir)

                        tabelaAtividades.addView(row)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mostrarMensagem("Erro: ${error.message}")
                }
            })

        btnVoltar.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        }
    }
}