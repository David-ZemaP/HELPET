package com.ucb.helpet.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ucb.helpet.features.login.domain.model.User
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onLogout: () -> Unit, viewModel: ProfileViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState() // Need to collect ProfileUiState

    LaunchedEffect(key1 = Unit) {
        viewModel.logoutEvent.collect {
            onLogout()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Publicación", tint = Color.White)
            }
        }
    ) { paddingValues ->

        // Check State
        when(val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProfileUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}")
                }
            }
            is ProfileUiState.Success -> {
                val user = state.user // Get user from state

                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val tabs = listOf("Publicaciones", "Ayudas", "Configuración")

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        ProfileHeader(user) // Pass User
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileStats()
                        Spacer(modifier = Modifier.height(16.dp))
                        TabRow(selectedTabIndex = selectedTabIndex) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(text = title) }
                                )
                            }
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> publicationsTab()
                        1 -> ayudasTab()
                        2 -> configuracionTab(viewModel, user) // Pass User
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = if (user.name.isNotEmpty()) user.name.take(2).uppercase() else "U"
                    Text(text = initials, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(text = user.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = if(user.userType.name == "VETERINARY") "Veterinario" else "Dueño de Mascota", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // You could add a bio field to User model later
            Text(
                text = "Miembro de la comunidad Helpet.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // DISPLAY REAL LOCATION
            if (user.location.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Text(text = user.location, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp), color = Color.White.copy(alpha = 0.9f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp), tint = Color.White)
                Text(text = "Editar", modifier = Modifier.padding(start = 8.dp), color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileStats() {
    // (Existing code)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(value = "0", label = "Mascotas Ayudadas")
        StatItem(value = "0 HELP", label = "Total Ganado", isHighlighted = true)
        StatItem(value = "0", label = "Publicaciones", isHighlighted = true, highlightColor = Color(0xFFFFA000))
    }
}

fun LazyListScope.publicationsTab() {
    item {
        Text("Mis Publicaciones", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
    }
    // Add mock items or empty state
}

fun LazyListScope.ayudasTab() {
    item {
        Text("Ayudas", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
    }
}

fun LazyListScope.configuracionTab(viewModel: ProfileViewModel, user: User) {
    item { // Personal Info section
        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(icon = Icons.Default.Email, text = user.email)

                // DISPLAY REAL PHONE
                if (user.phone.isNotEmpty()) {
                    InfoRow(icon = Icons.Default.Phone, text = user.phone)
                } else {
                    InfoRow(icon = Icons.Default.Phone, text = "No registrado")
                }

                // DISPLAY REAL LOCATION
                if (user.location.isNotEmpty()) {
                    InfoRow(icon = Icons.Default.LocationOn, text = user.location)
                } else {
                    InfoRow(icon = Icons.Default.LocationOn, text = "No registrada")
                }
            }
        }
    }
    item { // Account settings section
        Text(
            text = "Configuración de Cuenta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                SettingsSwitch(title = "Notificaciones por email", initialChecked = true)
                SettingsSwitch(title = "Notificaciones push", initialChecked = true)
                SettingsSwitch(title = "Perfil público", initialChecked = true, isPublic = true)
            }
        }
    }
    item { // Logout button
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.onLogoutClicked() },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar Sesión")
        }
        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun StatItem(value: String, label: String, isHighlighted: Boolean = false, highlightColor: Color = Color(0xFF00C853)) {
    // (Same as before)
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(4.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp).width(80.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) highlightColor else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SettingsSwitch(title: String, initialChecked: Boolean, isPublic: Boolean = false) {
    var checked by remember { mutableStateOf(initialChecked) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        if (isPublic) {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(if(checked) "Público" else "Privado")
            }
        } else {
            Switch(checked = checked, onCheckedChange = { checked = it })
        }
    }
}
