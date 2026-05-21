package com.santoos30.spotifyapi2bi

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import model.Resultado

class HistoricoActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var listaHistorico: LinearLayout
    private lateinit var txtPontuacao: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        database = FirebaseDatabase.getInstance()
        txtPontuacao = findViewById(R.id.txtPontuacao)
        listaHistorico = findViewById(R.id.listaHistorico)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        }
        carregarHistorico()
        val btnLimpar = findViewById<ImageView>(R.id.btnLimpar)

        btnLimpar.setOnClickListener {
            database.getReference("resultados").removeValue().addOnSuccessListener {
                    mostrarMensagem("Histórico apagado!")
                }
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

    private fun carregarHistorico() {
        database.getReference("resultados").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaHistorico.removeAllViews()
                    var pontos = 0
                    var total = 0
                    for (item in snapshot.children) {
                        val resultado = item.getValue(Resultado::class.java)
                        if (resultado != null) {
                            total++
                            if (resultado.acertou) {
                                pontos++
                            }
                            val texto = TextView(this@HistoricoActivity)
                            texto.text = "Criança: ${resultado.criancaNome}\n" +
                                         "Atividade: ${resultado.atividadeTitulo}\n" +
                                         "Acertou: ${if(resultado.acertou) "Sim" else "Não"}" +
                                        (if (!resultado.acertou) {
                                            "\nResposta dada: ${resultado.respostaRecebida}"
                                        }else{ "" })
                            texto.textSize = 18f
                            texto.setTextColor(android.graphics.Color.parseColor("#A0F0E8"))
                            texto.typeface = androidx.core.content.res.ResourcesCompat.getFont(this@HistoricoActivity, R.font.allerta)
                            texto.paint.isFakeBoldText = true
                            texto.setShadowLayer(7f,0f, 0f, android.graphics.Color.parseColor("#004d41"))
                            texto.background = getDrawable(R.drawable.input_bg)
                            texto.setPadding(25, 25, 25, 25)

                            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            params.setMargins(0, 0, 0, 25)
                            texto.layoutParams = params
                            listaHistorico.addView(texto)
                        }
                    }
                    txtPontuacao.text = "Pontuação: $pontos acertos de $total atividades"
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}