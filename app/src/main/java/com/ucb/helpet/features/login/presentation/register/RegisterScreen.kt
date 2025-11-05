package com.ucb.helpet.features.login.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ucb.helpet.utils.Resource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = koinViewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(UserType.CLIENT) }
    var license by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var userTypeExpanded by remember { mutableStateOf(false) }
    val userTypes = listOf("Dueño de Mascota", "Veterinario")

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is RegisterUiState.Success -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
                viewModel.resetState()
            }
            is RegisterUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                 viewModel.resetState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Únete a Helpet", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Crea tu cuenta y empieza a ayudar a mascotas perdidas",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        ExposedDropdownMenuBox(
            expanded = userTypeExpanded,
            onExpandedChange = { userTypeExpanded = !userTypeExpanded }
        ) {
            OutlinedTextField(
                value = if (userType == UserType.VETERINARY) "Veterinario" else "Dueño de Mascota",
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Usuario *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userTypeExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = userTypeExpanded,
                onDismissRequest = { userTypeExpanded = false }
            ) {
                userTypes.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            userType = if (selectionOption == "Veterinario") UserType.VETERINARY else UserType.CLIENT
                            userTypeExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Completo *") }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono (Opcional)") }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ubicación (Opcional)") }, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) })
        Spacer(modifier = Modifier.height(8.dp))

        if (userType == UserType.VETERINARY) {
            OutlinedTextField(value = license, onValueChange = { license = it }, label = { Text("Número de Licencia Veterinaria *") }, modifier = Modifier.fillMaxWidth())
            Text("Tu licencia será verificada para confirmar tu perfil profesional", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp))
        }

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña *") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) })
        Text("Mínimo 8 caracteres", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp))

        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar Contraseña *") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) })
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
            val annotatedString = buildAnnotatedString {
                append("Acepto los ")
                pushStringAnnotation(tag = "TERMS", annotation = "terms")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("términos y condiciones")
                }
                pop()
                append(" y ")
                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("política de privacidad")
                }
                pop()
            }
            ClickableText(text = annotatedString, onClick = { offset ->
                // TODO: Handle clicks on "terms" and "privacy"
            })
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { 
                if (password == confirmPassword) {
                    viewModel.register(name, email, password, userType)
                } else {
                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }, 
            modifier = Modifier.fillMaxWidth(), 
            enabled = termsAccepted && uiState !is RegisterUiState.Loading
        ) {
            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Crear Cuenta")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Divider(modifier = Modifier.weight(1f))
            Text("o", modifier = Modifier.padding(horizontal = 8.dp))
            Divider(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = { /* TODO: Handle Google Sign In */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrarse con Google", modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        val loginText = buildAnnotatedString {
            append("¿Ya tienes una cuenta? ")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("Inicia sesión aquí")
            }
        }
        ClickableText(
            text = loginText,
            onClick = { navController.navigate("login") }
        )
    }
}

// Need to add NavController to Preview or create a version without navigation
// @Preview(showBackground = true)
// @Composable
// fun RegisterScreenPreview() {
//     RegisterScreen(navController = rememberNavController())
// }
