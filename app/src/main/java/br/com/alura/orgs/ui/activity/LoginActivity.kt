package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.databinding.ActivityLoginBinding
import br.com.alura.orgs.extensions.vaiPara
import kotlinx.coroutines.launch

class LoginActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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

            Log.i("LoginActivity", "configuraBotaoEntrar: $usuarioNome, $senha")
            lifecycleScope.launch {
                autentica(usuarioNome, senha)
            }
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioUsuarioActivity::class.java)
        }
    }
}