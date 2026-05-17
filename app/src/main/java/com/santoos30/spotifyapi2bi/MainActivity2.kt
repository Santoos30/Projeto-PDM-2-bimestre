package com.santoos30.spotifyapi2bi

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    private var GlobalSenhaVisivel = false
    private lateinit var GlobalEtEmail: EditText
    private lateinit var GlobalEtSenha: EditText
    private lateinit var GlobalBtnRegis: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val Senha = findViewById<EditText>(R.id.SenhaRegis)
        val Olho = findViewById<ImageView>(R.id.MostrarSenha)
        val Login = findViewById<TextView>(R.id.Logar)
        GlobalEtEmail = findViewById(R.id.EmailRegis)
        GlobalEtSenha = findViewById(R.id.SenhaRegis)
        GlobalBtnRegis = findViewById(R.id.BotaoRegis)

        Login.setOnClickListener {
            val TelaEntrada = Intent(this, MainActivity::class.java)
            startActivity(TelaEntrada)
        }

        GlobalBtnRegis.setOnClickListener {
            val email = GlobalEtEmail.text.toString().trim()
            val password = GlobalEtSenha.text.toString().trim()
            Cadastrar(email, password)
        }

        Olho.setOnClickListener {
            GlobalSenhaVisivel = !GlobalSenhaVisivel
            if (!GlobalSenhaVisivel) {
                GlobalEtSenha.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                GlobalEtSenha.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            GlobalEtSenha.setSelection(GlobalEtSenha.text.length)
        }
    }

    fun Cadastrar(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "${user?.email}, cadastrado com sucesso.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // Falha no registro
                    Toast.makeText(this, "Erro ao Cadastrar, ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}