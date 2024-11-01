package br.com.alura.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.databinding.ActivityPerfilUsuarioBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity: UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnSairConfig()
        preencherCampos()
    }

    private fun preencherCampos() {
        lifecycleScope.launch {
            usuario.filterNotNull().collect { usuarioLogado ->
                binding.nomeTextBox.text = usuarioLogado.nome
                binding.usuarioTextBox.text = usuarioLogado.id
            }
        }
    }

    private fun btnSairConfig() {
        binding.button.setOnClickListener {
            lifecycleScope.launch {
                deslogaUsuario()
                finish()
            }
        }
    }
}