package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.database.dataStore
import br.com.alura.orgs.database.usuarioLogadoPreferences
import br.com.alura.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private var url: String? = null
    private var produtoId = 0L
    private val produtoDao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar produto"
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this)
                .mostra(url) { imagem ->
                    url = imagem
                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
                }
        }
        tentaCarregarProduto()
        lifecycleScope.launch {
            dataStore.data.collect { preferences ->
                preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                    buscaUsuario(usuarioId)
                }
            }
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    override fun onResume() {
        super.onResume()
        tentaBuscarProduto()
    }

    private fun tentaBuscarProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId).first().let { produto ->
                produto?.let { produtoEncontrado ->
                    title = "Alterar produto"
                    val campoUsuarioId = binding.activityFormularioProdutoUsuario
                    campoUsuarioId.visibility =
                        if (produtoEncontrado.salvoSemUsuario()) {
                            configuraCampoUsuario()
                            VISIBLE
                        } else {
                            GONE
                        }
                    preencheCampos(produtoEncontrado)
                }
            }
        }
    }

    private fun preencheCampos(produto: Produto) {
        url = produto.imagem
        binding.activityFormularioProdutoImagem
            .tentaCarregarImagem(produto.imagem)
        binding.activityFormularioProdutoNome
            .setText(produto.nome)
        binding.activityFormularioProdutoDescricao
            .setText(produto.descricao)
        binding.activityFormularioProdutoValor
            .setText(produto.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar

        botaoSalvar.setOnClickListener {

            lifecycleScope.launch {
                usuario.value?.let { usuario ->
                    val produtoNovo = criaProduto(usuario.id)
                    produtoDao.salvar(produtoNovo)
                    finish()
                }
            }
        }
    }

    fun configuraCampoUsuario() {
        lifecycleScope.launch {
            usuarios().map { usuarios ->
                usuarios.map {
                    it.id }
            }.collect { usuarios ->
                    configuraAutoCompleteTextView(usuarios)
            }
        }
    }

    private fun configuraAutoCompleteTextView(usuarios: List<String>) {
        val campoUsuarioId = binding.activityFormularioProdutoUsuario
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usuarios)
        campoUsuarioId.setAdapter(adapter)
        campoUsuarioId.threshold = 0
    }

    private fun criaProduto(usuarioId: String): Produto {
        val nome = binding.activityFormularioProdutoNome.text.toString()
        val descricao = binding.activityFormularioProdutoDescricao.text.toString()
        val valorEmTexto = binding.activityFormularioProdutoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url,
            usuarioId = usuarioId
        )
    }
}