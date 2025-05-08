package com.jsborbon.reparalo.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    fun deleteAppData(context: Context) {
        viewModelScope.launch(ioDispatcher) {
            context.deleteDatabase("Reparalo.db")
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Datos eliminados correctamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
