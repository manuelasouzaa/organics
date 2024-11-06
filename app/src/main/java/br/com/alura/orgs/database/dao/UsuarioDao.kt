package br.com.alura.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.alura.orgs.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun salva(user: Usuario)

    @Query("SELECT * FROM Usuario WHERE id = :usuarioId AND senha = :senha")
    fun autentica(usuarioId: String, senha: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE id = :usuarioId")
    fun buscaUsuarioPorId(usuarioId: String): Flow<Usuario>

    @Query("SELECT * FROM Usuario")
    fun buscaTodos(): Flow<List<Usuario>>

}