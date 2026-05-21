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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {
    private var GlobalSenhaVisivel = false
    private lateinit var GlobalEtEmail: EditText
    private lateinit var GlobalEtSenha: EditText
    private lateinit var GlobalBtnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        GlobalEtEmail = findViewById(R.id.EmailLogin)
        GlobalEtSenha = findViewById(R.id.SenhaLogin)
        GlobalBtnLogin = findViewById(R.id.BotaoLogin)

        val txtEsqueciSenha = findViewById<TextView>(R.id.txtEsqueciSenha)
        val Olho = findViewById<ImageView>(R.id.MostrarSenha)
        val Regis = findViewById<TextView>(R.id.Registrar)

        val credentialManager = CredentialManager.create(this)
        val BtnGoogle = findViewById<ImageView>(R.id.LoginGoogle)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        txtEsqueciSenha.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_recuperar_senha, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
            val editEmail = dialogView.findViewById<EditText>(R.id.editEmailRecuperacao)
            val btnEnviar = dialogView.findViewById<Button>(R.id.btnEnviarRecuperacao)

            btnEnviar.setOnClickListener {
                val email = editEmail.text.toString().trim()
                if (email.isEmpty()) {
                    mostrarMensagem("Digite um email!")
                    return@setOnClickListener
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                        dialog.dismiss()
                        if (it.isSuccessful) {
                            dialog.dismiss()
                            mostrarMensagem("Verifique seu email para redefinir sua senha.")
                        } else {
                            mostrarMensagem("Erro: ${it.exception?.message}")
                        }
                    }
            }
        }

        BtnGoogle.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(request = request, context = this@LoginActivity)
                    handleSignIn(result)
                } catch (e: Exception) {
                    mostrarMensagem("Erro ao entrar com Google")
                }
            }
        }

        Regis.setOnClickListener {
            val TelaCadastro = Intent(this, RegisterActivity::class.java)
            startActivity(TelaCadastro)
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        }

        GlobalBtnLogin.setOnClickListener {
            val Email = GlobalEtEmail.text.toString().trim()
            val Senha = GlobalEtSenha.text.toString().trim()
            Entrar(Email, Senha)
        }

        Olho.setOnClickListener {
            GlobalSenhaVisivel = !GlobalSenhaVisivel
            if (GlobalSenhaVisivel) {
                GlobalEtSenha.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                GlobalEtSenha.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            GlobalEtSenha.setSelection(GlobalEtSenha.text.length)
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

    private fun Entrar(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensagem("Preencha todos os campos!")
            return
        }
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val telaHome = Intent(this, HomeActivity::class.java)
                    startActivity(telaHome)
                    finish()
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                } else {
                    val mensagemErro = when {
                        task.exception?.message?.contains("badly formatted", ignoreCase = true) == true -> {
                            "O email digitado é inválido!"
                        }
                        task.exception?.message?.contains("password is invalid", ignoreCase = true) == true -> {
                            "Senha incorreta!"
                        }

                        task.exception?.message?.contains("no user record", ignoreCase = true) == true -> {
                            "Nenhuma conta encontrada com esse email!"
                        }
                        task.exception?.message?.contains("network error", ignoreCase = true) == true -> {
                            "Sem conexão com a internet!"
                        }
                        else -> {
                            "Erro ao entrar. Tente novamente!"
                        }
                    }
                    mostrarMensagem(mensagemErro)
                }
            }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        if (
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(firebaseCredential).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val telaHome = Intent(this, HomeActivity::class.java)
                            startActivity(telaHome)
                            finish()
                            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                        } else {
                            mostrarMensagem("Erro ao entrar com Google")
                        }
                    }
            } catch (e: GoogleIdTokenParsingException) {
                mostrarMensagem("Erro de Token Google")
            }
        }
    }
}