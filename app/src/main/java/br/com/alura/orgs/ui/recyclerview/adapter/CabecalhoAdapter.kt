package br.com.alura.orgs.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.orgs.databinding.ActivityCabecalhoBinding

class CabecalhoAdapter(
    private val context: Context,
    usuario: List<String?> = emptyList(),
) : RecyclerView.Adapter<CabecalhoAdapter.ViewHolder>(){

        private val usuarios: List<String?> = usuario.toMutableList()

   class ViewHolder (private val binding: ActivityCabecalhoBinding):
       RecyclerView.ViewHolder(binding.root) {

        fun vincula(usuario: String?) {
            binding.usuarioNome.text = usuario
        }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ActivityCabecalhoBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario =
        if (usuarios[position].isNullOrBlank())
            "Sem usuário"
        else {
            usuarios[position].toString()
        }
        holder.vincula(usuario)
    }

    override fun getItemCount() = usuarios.size
}
