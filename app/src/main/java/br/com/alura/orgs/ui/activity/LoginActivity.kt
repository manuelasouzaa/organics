package br.com.alura.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.database.dataStore
import br.com.alura.orgs.database.usuarioLogadoPreferences
import br.com.alura.orgs.databinding.ActivityLoginBinding
import br.com.alura.orgs.extensions.toast
import br.com.alura.orgs.extensions.vaiPara
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuarioNome = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            autentica(usuarioNome, senha)
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioUsuarioActivity::class.java)
        }
    }

    fun autentica(usuario: String, senha: String) {
        CoroutineScope(Dispatchers.IO).launch {
            usuarioDao.autentica(usuario, senha)?.let { usuario ->
                launch {
                    dataStore.edit { preferences ->
                        preferences[usuarioLogadoPreferences] = usuario.id
                    }
                }
                vaiPara(ListaProdutosActivity::class.java) {
                    usuarioLogadoPreferences
                }
                finish()
            } ?: run {
                withContext(Dispatchers.Main.immediate) {
                    toast("Falha na autenticação")
                }
            }
        }
    }
}