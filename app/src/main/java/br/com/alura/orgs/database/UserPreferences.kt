package br.com.alura.orgs.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.alura.orgs.ui.activity.DS

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DS)

val usuarioLogadoPreferences = stringPreferencesKey("usuario_logado")