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
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
                    row.addView(txtId)
                    row.addView(txtNome)
                    row.addView(txtIdade)
                    row.addView(txtLeitura)
                    row.addView(txtMatematica)

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