import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.components.NavigationBottomBar
import com.jsborbon.reparalo.navigation.Routes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val usuario by viewModel.usuario.collectAsState()

    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "") }
    var disponibilidad by remember { mutableStateOf(usuario?.disponibilidad ?: "") }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBottomBar(selectedIndex = 4, navController = navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = disponibilidad,
                onValueChange = { disponibilidad = it },
                label = { Text("Disponibilidad") },
                placeholder = { Text("Ej: Lunes a Viernes 8:00 a 18:00") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = usuario?.email ?: "",
                onValueChange = {},
                label = { Text("Correo electrónico") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = usuario?.tipoUsuario ?: "",
                onValueChange = {},
                label = { Text("Tipo de Usuario") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.actualizarUsuario(
                        nombre = nombre,
                        telefono = telefono,
                        disponibilidad = disponibilidad,
                        onSuccess = { mensaje = "Cambios guardados correctamente" },
                        onError = { mensaje = it },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Guardar Cambios")
            }

            TextButton(onClick = {
                usuario?.email?.let { email ->
                    viewModel.enviarResetPassword(
                        email = email,
                        onSuccess = { mensaje = "Se envió un correo para cambiar la contraseña." },
                        onError = { mensaje = it },
                    )
                }
            }) {
                Text("Cambiar contraseña")
            }

            Spacer(Modifier.height(16.dp))

            mensaje?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(Routes.AUTHENTICATION) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
