package com.santoos30.spotifyapi2bi

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import model.Crianca
import  android.widget.TextView
import android.widget.Toast
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat

class GetCrianActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var tabelaCriancas: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_get_crian)
        database = FirebaseDatabase.getInstance()
        tabelaCriancas = findViewById(R.id.tabelaCriancas)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        database.getReference("criancas").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (tabelaCriancas.childCount > 1) {
                    tabelaCriancas.removeViews(1, tabelaCriancas.childCount - 1)
                }
                for (item in snapshot.children) {
                    val crianca = item.getValue(Crianca::class.java)
                    val row = TableRow(this@GetCrianActivity)
                    val params = TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    fun criarTextView(texto: String): TextView {
                        return TextView(this@GetCrianActivity).apply {
                            text = texto
                            layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            setPadding(12, 8, 12, 8)
                            textSize = 18f
                            typeface = ResourcesCompat.getFont(context, R.font.allerta)
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

                            gravity = android.view.Gravity.CENTER

                            setSingleLine(true)
                        }
                    }
                    val txtId = criarTextView(
                        crianca?.id ?.replace("-", "") ?.take(5) ?: ""
                    )
                    val txtNome = criarTextView(
                        crianca?.nome ?: ""
                    )
                    val txtIdade = criarTextView(
                        crianca?.idade.toString()
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
                        android.graphics.Color.parseColor("#FF4C4C")
                    )
                    btnExcluir.setPadding(12, 8, 12, 8)

                    btnExcluir.setOnClickListener {
                        crianca?.id?.let { id ->
                            database
                                .getReference("criancas")
                                .child(id)
                                .removeValue()
                            Toast.makeText(this@GetCrianActivity, "Criança removida!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    val btnEditar = TextView(this@GetCrianActivity)

                    btnEditar.text = "✐"
                    btnEditar.textSize = 20f
                    btnEditar.setTextColor(
                        android.graphics.Color.parseColor("#FFC107")
                    )
                    btnEditar.setPadding(12, 8, 12, 8)
                    btnEditar.setOnClickListener {
                        val dialogView = layoutInflater.inflate(
                            R.layout.dialog_editar_crian,
                            null
                        )
                        val dialog = AlertDialog.Builder(this@GetCrianActivity)
                            .setView(dialogView)
                            .create()
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.show()
                        val btnSalvar =
                            dialogView.findViewById<TextView>(R.id.btnSalvarDialog)
                        val btnCancelar =
                            dialogView.findViewById<TextView>(R.id.btnCancelarDialog)
                        val editNome =
                            dialogView.findViewById<EditText>(R.id.editNomeDialog)
                        val editIdade =
                            dialogView.findViewById<EditText>(R.id.editIddDialog)
                        val editLeitura =
                            dialogView.findViewById<EditText>(R.id.editLeituraDialog)
                        val editMatematica =
                            dialogView.findViewById<EditText>(R.id.editMatematicaDialog)
                        editNome.setText(crianca?.nome)
                        editIdade.setText(crianca?.idade.toString())
                        editLeitura.setText(crianca?.nivelLeitura)
                        editMatematica.setText(crianca?.nivelMatematica)
                        btnCancelar.setOnClickListener {
                            dialog.dismiss()
                        }

                        btnSalvar.setOnClickListener {

                            val novosDados = mapOf(
                                "nome" to editNome.text.toString(),
                                "idade" to editIdade.text.toString().toInt(),
                                "nivelLeitura" to editLeitura.text.toString(),
                                "nivelMatematica" to editMatematica.text.toString()
                            )
                            crianca?.id?.let { id ->
                                database
                                    .getReference("criancas")
                                    .child(id)
                                    .updateChildren(novosDados)

                                Toast.makeText(
                                    this@GetCrianActivity, "Criança atualizada com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
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

                Toast.makeText(
                    this@GetCrianActivity,
                    "Erro: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        btnVoltar.setOnClickListener{
            finish ()
        }
    }
}