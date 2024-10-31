package br.com.alura.orgs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(
    @PrimaryKey val id: String,
    @ColumnInfo (name = "nome") val nome: String,
    @ColumnInfo (name = "senha") val senha: String
)
