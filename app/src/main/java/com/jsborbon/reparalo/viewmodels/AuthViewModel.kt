import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jsborbon.reparalo.data.AuthRepository
import com.jsborbon.reparalo.data.UsuarioRepository
import com.jsborbon.reparalo.models.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usuarioRepository: UsuarioRepository,
) : ViewModel() {

    val authState = MutableStateFlow<FirebaseUser?>(null)
    val usuario = MutableStateFlow<Usuario?>(null)

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val user = authRepository.signIn(email, password)
            authState.value = user
            if (user != null) {
                usuario.value = authRepository.getUsuarioData(user.uid)
                onSuccess()
            } else {
                onError("Credenciales inválidas o conexión fallida")
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        nombre: String,
        telefono: String,
        tipoUsuario: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val user = authRepository.signUp(email, password, nombre, telefono, tipoUsuario)
            authState.value = user
            if (user != null) {
                usuario.value = authRepository.getUsuarioData(user.uid)
                onSuccess()
            } else {
                onError("No se pudo completar el registro")
            }
        }
    }

    fun actualizarUsuario(
        nombre: String,
        telefono: String,
        disponibilidad: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val user = authState.value ?: return@launch onError("Usuario no autenticado")
            val actual = usuario.value ?: return@launch onError("Datos de usuario no cargados")

            val actualizado = actual.copy(
                nombre = nombre,
                telefono = telefono,
                disponibilidad = disponibilidad,
            )

            val ok = usuarioRepository.actualizarUsuario(user.uid, actualizado)
            if (ok) {
                usuario.value = actualizado
                onSuccess()
            } else {
                onError("Error al guardar cambios")
            }
        }
    }

    fun enviarResetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val enviado = try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
                true
            } catch (e: Exception) {
                false
            }

            if (enviado) onSuccess() else onError("No se pudo enviar el correo. Inténtalo más tarde.")
        }
    }

    fun logout() {
        authRepository.signOut()
        authState.value = null
        usuario.value = null
    }
}
