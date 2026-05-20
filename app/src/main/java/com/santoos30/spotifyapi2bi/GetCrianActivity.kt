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
import model.Crianca

class GetCrianActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var tabelaCriancas: TableLayout

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
        setContentView(R.layout.activity_get_crian)

        database = FirebaseDatabase.getInstance()

        tabelaCriancas = findViewById(R.id.tabelaCriancas)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)

        database.getReference("criancas")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (tabelaCriancas.childCount > 1) {

                        tabelaCriancas.removeViews(
                            1,
                            tabelaCriancas.childCount - 1
                        )
                    }

                    for (item in snapshot.children) {

                        val crianca = item.getValue(Crianca::class.java)

                        val row = TableRow(this@GetCrianActivity)

                        fun criarTextView(texto: String): TextView {

                            return TextView(this@GetCrianActivity).apply {

                                text = texto

                                setPadding(12, 8, 12, 8)

                                textSize = 18f

                                typeface = ResourcesCompat.getFont(
                                    context,
                                    R.font.allerta
                                )

                                paint.isFakeBoldText = true

                                setTextColor(
                                    Color.parseColor("#00A88F")
                                )

                                setShadowLayer(
                                    7f,
                                    0f,
                                    0f,
                                    Color.parseColor("#004d41")
                                )

                                gravity = Gravity.CENTER

                                setSingleLine(true)
                            }
                        }

                        val txtId = criarTextView(
                            crianca?.id
                                ?.replace("-", "")
                                ?.take(5) ?: ""
                        )

                        val txtNome = criarTextView(
                            crianca?.nome ?: ""
                        )

                        val txtIdade = criarTextView(
                            crianca?.idade?.toString() ?: ""
                        )

                        val txtLeitura = criarTextView(
                            crianca?.nivelLeitura ?: ""
                        )

                        val txtMatematica = criarTextView(
                            crianca?.nivelMatematica ?: ""
                        )

                        val btnExcluir = TextView(this@GetCrianActivity)

                        btnExcluir.text = "X"

                        btnExcluir.textSize = 20f

                        btnExcluir.setTextColor(
                            Color.parseColor("#FF4C4C")
                        )

                        btnExcluir.setPadding(12, 8, 12, 8)

                        btnExcluir.setOnClickListener {

                            crianca?.id?.let { id ->

                                database
                                    .getReference("criancas")
                                    .child(id)
                                    .removeValue()

                                mostrarMensagem(
                                    "Criança removida!"
                                )
                            }
                        }

                        val btnEditar = TextView(this@GetCrianActivity)

                        btnEditar.text = "✐"

                        btnEditar.textSize = 20f

                        btnEditar.setTextColor(
                            Color.parseColor("#FFC107")
                        )

                        btnEditar.setPadding(12, 8, 12, 8)

                        btnEditar.setOnClickListener {

                            val dialogView = layoutInflater.inflate(
                                R.layout.dialog_editar_crian,
                                null
                            )

                            val dialog = AlertDialog.Builder(
                                this@GetCrianActivity
                            )
                                .setView(dialogView)
                                .create()

                            dialog.window?.setBackgroundDrawableResource(
                                android.R.color.transparent
                            )

                            dialog.show()

                            val btnSalvar =
                                dialogView.findViewById<TextView>(
                                    R.id.btnSalvarDialog
                                )

                            val btnCancelar =
                                dialogView.findViewById<TextView>(
                                    R.id.btnCancelarDialog
                                )

                            val editNome =
                                dialogView.findViewById<EditText>(
                                    R.id.editNomeDialog
                                )

                            val editIdade =
                                dialogView.findViewById<EditText>(
                                    R.id.editIddDialog
                                )

                            val editLeitura =
                                dialogView.findViewById<EditText>(
                                    R.id.editLeituraDialog
                                )

                            val editMatematica =
                                dialogView.findViewById<EditText>(
                                    R.id.editMatematicaDialog
                                )

                            editNome.setText(
                                crianca?.nome
                            )

                            editIdade.setText(
                                crianca?.idade?.toString() ?: ""
                            )

                            editLeitura.setText(
                                crianca?.nivelLeitura
                            )

                            editMatematica.setText(
                                crianca?.nivelMatematica
                            )

                            btnCancelar.setOnClickListener {
                                dialog.dismiss()
                            }

                            btnSalvar.setOnClickListener {

                                val nome =
                                    editNome.text.toString().trim()

                                val idadeTexto =
                                    editIdade.text.toString().trim()

                                val leitura =
                                    editLeitura.text.toString().trim()

                                val matematica =
                                    editMatematica.text.toString().trim()

                                if (
                                    nome.isEmpty() ||
                                    idadeTexto.isEmpty() ||
                                    leitura.isEmpty() ||
                                    matematica.isEmpty()
                                ) {

                                    mostrarMensagem(
                                        "preencha todos os campos!"
                                    )

                                    return@setOnClickListener
                                }

                                val idade =
                                    idadeTexto.toIntOrNull()

                                if (idade == null) {

                                    mostrarMensagem(
                                        "Digite uma idade válida!"
                                    )

                                    return@setOnClickListener
                                }

                                val novosDados = mapOf(

                                    "nome" to nome,

                                    "idade" to idade,

                                    "nivelLeitura" to leitura,

                                    "nivelMatematica" to matematica
                                )

                                crianca?.id?.let { id ->

                                    database
                                        .getReference("criancas")
                                        .child(id)
                                        .updateChildren(novosDados)

                                    mostrarMensagem(
                                        "Criança atualizada!"
                                    )
                                }

                                dialog.dismiss()
                            }
                        }

                        row.addView(txtId)
                        row.addView(txtNome)
                        row.addView(txtIdade)
                        row.addView(txtLeitura)
                        row.addView(txtMatematica)
                        row.addView(btnEditar)
                        row.addView(btnExcluir)

                        tabelaCriancas.addView(row)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    mostrarMensagem(
                        "Erro: $error"
                    )
                }
            })

        btnVoltar.setOnClickListener {

            finish()

            overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
            )
        }
    }
}