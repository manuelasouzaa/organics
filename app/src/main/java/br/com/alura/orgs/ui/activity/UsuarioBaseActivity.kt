package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.database.dataStore
import br.com.alura.orgs.database.usuarioLogadoPreferences
import br.com.alura.orgs.extensions.vaiPara
import br.com.alura.orgs.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity: AppCompatActivity() {
    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }
    private val _usuario: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    protected val usuario: StateFlow<Usuario?> = _usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch{
            verificaUsuarioLogado()
        }
    }

    protected suspend fun verificaUsuarioLogado() {
        dataStore.data.first().let { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)
                } ?: vaiParaLogin()
            }
    }

    protected suspend fun buscaUsuario(usuarioId: String): Usuario? {
        return usuarioDao.buscaUsuarioPorId(usuarioId).firstOrNull().also {
            _usuario.value = it
        }
    }

    protected suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(usuarioLogadoPreferences)
        }
    }

    protected fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    protected fun usuarios() = usuarioDao.buscaTodos()
}