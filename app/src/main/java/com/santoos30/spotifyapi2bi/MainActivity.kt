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
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private var GlobalSenhaVisivel = false
    private lateinit var GlobalEtEmail: EditText
    private lateinit var GlobalEtSenha: EditText
    private lateinit var GlobalBtnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        val Senha = findViewById<EditText>(R.id.SenhaLogin)
        val Olho = findViewById<ImageView>(R.id.MostrarSenha)
        val Regis = findViewById<TextView>(R.id.Registrar)
        GlobalEtEmail = findViewById(R.id.EmailLogin)
        GlobalEtSenha = findViewById(R.id.SenhaLogin)
        GlobalBtnLogin = findViewById(R.id.BotaoLogin)


        Regis.setOnClickListener {
            val TelaCadastro = Intent(this, MainActivity2::class.java)
            startActivity (TelaCadastro)
        }

        GlobalBtnLogin.setOnClickListener {
            val Email = GlobalEtEmail.text.toString().trim()
            val Senha = GlobalEtSenha.text.toString().trim()
            Entrar(Email, Senha)
        }

        Olho.setOnClickListener {
            GlobalSenhaVisivel = !GlobalSenhaVisivel
            if (GlobalSenhaVisivel) {
                GlobalEtSenha.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                GlobalEtSenha.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            GlobalEtSenha.setSelection(GlobalEtSenha.text.length)
        }
    }
    private fun Entrar(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "${user?.email}, entrou com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "Erro ao entrar, ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}