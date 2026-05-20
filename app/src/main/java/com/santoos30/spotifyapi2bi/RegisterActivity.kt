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

class RegisterActivity : AppCompatActivity() {
    private var GlobalSenhaVisivel = false
    private lateinit var GlobalEtEmail: EditText
    private lateinit var GlobalEtSenha: EditText
    private lateinit var GlobalBtnRegis: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        GlobalEtEmail = findViewById(R.id.EmailRegis)
        GlobalEtSenha = findViewById(R.id.SenhaRegis)
        GlobalBtnRegis = findViewById(R.id.BotaoRegis)

        val Olho = findViewById<ImageView>(R.id.MostrarSenha)
        val Login = findViewById<TextView>(R.id.Logar)
        val credentialManager = CredentialManager.create(this)
        val BtnGoogle = findViewById<ImageView>(R.id.LoginGoogle)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        BtnGoogle.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(request = request, context = this@RegisterActivity)
                    handleSignIn(result)
                } catch (e: Exception) {
                    mostrarMensagem(
                        "Erro do Google: $e!"
                    )
                }
            }
        }

        Login.setOnClickListener {
            val TelaEntrada = Intent(this, LoginActivity::class.java)
            startActivity(TelaEntrada)
            overridePendingTransition(0, 0)
        }

        GlobalBtnRegis.setOnClickListener {
            val email = GlobalEtEmail.text.toString().trim()
            val password = GlobalEtSenha.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                mostrarMensagem(
                    "Preencha todos os campos!"
                )
                return@setOnClickListener
            }
            if (
                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {
                mostrarMensagem(
                    "Digite um email válido!"
                )
                return@setOnClickListener
            }
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

    private fun mostrarMensagem(mensagem: String) {

        val dialogView = layoutInflater.inflate(
            R.layout.dialog_mensagem,
            null
        )

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(
            android.R.color.transparent
        )

        dialog.show()

        val txtMensagem =
            dialogView.findViewById<TextView>(
                R.id.txtMensagemDialog
            )

        val btnOk =
            dialogView.findViewById<Button>(
                R.id.btnOkDialog
            )

        txtMensagem.text = mensagem

        btnOk.setOnClickListener {
            dialog.dismiss()
        }
    }


    fun Cadastrar(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val telaHome = Intent(this, HomeActivity::class.java)
                    startActivity(telaHome)
                    finish()
                    overridePendingTransition(
                        R.anim.slide_in,
                        R.anim.slide_out
                    )
                } else {
                    mostrarMensagem(
                        "Erro ao cadastrar:\n${task.exception?.message}"
                    )
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
                FirebaseAuth.getInstance()
                    .signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            mostrarMensagem(
                                "${user?.email} entrou pelo Google com sucesso."
                            )
                            val telaHome = Intent(this, HomeActivity::class.java)
                            startActivity(telaHome)
                            finish()
                            overridePendingTransition(
                                R.anim.slide_in,
                                R.anim.slide_out
                            )
                        } else {
                            mostrarMensagem(
                                "Erro ao entrar com Google."
                            )
                        }
                    }
            } catch (e: GoogleIdTokenParsingException) {
                mostrarMensagem(
                    "Erro de Token Google."
                )
            }
        }
    }
}