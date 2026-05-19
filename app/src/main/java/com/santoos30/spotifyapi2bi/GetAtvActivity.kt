package com.santoos30.spotifyapi2bi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import model.Atividade

class GetAtvActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var tabelaAtividades: TableLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_atv)

        database = FirebaseDatabase.getInstance()

        tabelaAtividades =
            findViewById(R.id.tabelaAtividades)

        val btnVoltar =
            findViewById<Button>(R.id.btnVoltar)

        database
            .getReference("atividades")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (tabelaAtividades.childCount > 1) {

                        tabelaAtividades.removeViews(
                            1,
                            tabelaAtividades.childCount - 1
                        )
                    }

                    for (item in snapshot.children) {

                        val atividade =
                            item.getValue(Atividade::class.java)

                        val row =
                            TableRow(this@GetAtvActivity)

                        fun criarTextView(texto: String): TextView {

                            return TextView(this@GetAtvActivity).apply {

                                text = texto

                                setPadding(12, 8, 12, 8)

                                textSize = 18f

                                typeface =
                                    ResourcesCompat.getFont(
                                        context,
                                        R.font.allerta
                                    )

                                paint.isFakeBoldText = true

                                setTextColor(
                                    android.graphics.Color.parseColor("#00A88F")
                                )

                                setShadowLayer(
                                    7f,
                                    0f,
                                    0f,
                                    android.graphics.Color.parseColor("#004d41")
                                )

                                gravity =
                                    android.view.Gravity.CENTER

                                setSingleLine(true)
                            }
                        }

                        val txtId =
                            criarTextView(
                                atividade?.id
                                    ?.replace("-", "")
                                    ?.take(5) ?: ""
                            )

                        val txtTitulo =
                            criarTextView(
                                atividade?.titulo ?: ""
                            )

                        val txtTipo =
                            criarTextView(
                                atividade?.tipo ?: ""
                            )

                        val txtDificuldade =
                            criarTextView(
                                atividade?.dificuldade ?: ""
                            )

                        val btnExcluir = TextView(this@GetAtvActivity)

                        btnExcluir.text = "X"
                        btnExcluir.textSize = 20f
                        btnExcluir.setTextColor(
                            android.graphics.Color.parseColor("#FF4C4C")
                        )
                        btnExcluir.setPadding(12, 8, 12, 8)

                        btnExcluir.setOnClickListener {

                            atividade?.id?.let { id ->

                                database
                                    .getReference("atividades")
                                    .child(id)
                                    .removeValue()

                                Toast.makeText(
                                    this@GetAtvActivity,
                                    "Atividade removida!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        val btnEditar = TextView(this@GetAtvActivity)

                        btnEditar.text = "✐"
                        btnEditar.textSize = 20f

                        btnEditar.setTextColor(
                            android.graphics.Color.parseColor("#FFC107")
                        )

                        btnEditar.setPadding(12, 8, 12, 8)

                        btnEditar.setOnClickListener {

                            val dialogView = layoutInflater.inflate(
                                R.layout.dialog_editar_atv,
                                null
                            )

                            val dialog = AlertDialog.Builder(this@GetAtvActivity)
                                .setView(dialogView)
                                .create()

                            dialog.window?.setBackgroundDrawableResource(
                                android.R.color.transparent
                            )

                            dialog.show()

                            val btnSalvar =
                                dialogView.findViewById<TextView>(R.id.btnSalvarDialog)

                            val btnCancelar =
                                dialogView.findViewById<TextView>(R.id.btnCancelarDialog)

                            val editTitulo =
                                dialogView.findViewById<EditText>(R.id.editTituloDialog)

                            val editTipo =
                                dialogView.findViewById<EditText>(R.id.editTipoDialog)

                            val editDificuldade =
                                dialogView.findViewById<EditText>(R.id.editDificuldadeDialog)

                            editTitulo.setText(atividade?.titulo)
                            editTipo.setText(atividade?.tipo)
                            editDificuldade.setText(atividade?.dificuldade)

                            btnCancelar.setOnClickListener {
                                dialog.dismiss()
                            }

                            btnSalvar.setOnClickListener {

                                val novosDados = mapOf(
                                    "titulo" to editTitulo.text.toString(),
                                    "tipo" to editTipo.text.toString(),
                                    "dificuldade" to editDificuldade.text.toString()
                                )

                                atividade?.id?.let { id ->

                                    database
                                        .getReference("atividades")
                                        .child(id)
                                        .updateChildren(novosDados)

                                    Toast.makeText(
                                        this@GetAtvActivity,
                                        "Atividade atualizada!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                dialog.dismiss()
                            }
                        }

                        row.addView(txtId)
                        row.addView(txtTitulo)
                        row.addView(txtTipo)
                        row.addView(txtDificuldade)
                        row.addView(btnEditar)
                        row.addView(btnExcluir)

                        tabelaAtividades.addView(row)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(
                        this@GetAtvActivity,
                        "Erro: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}