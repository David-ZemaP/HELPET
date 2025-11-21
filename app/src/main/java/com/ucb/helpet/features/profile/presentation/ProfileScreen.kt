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
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onLogout: () -> Unit, viewModel: ProfileViewModel = koinViewModel()) {
    LaunchedEffect(key1 = Unit) {
        viewModel.logoutEvent.collect {
            onLogout()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = MaterialTheme.colorScheme.secondary // Usa el GreenAction (#34D399)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Publicación", tint = Color.White)
            }
        }
    ) { paddingValues ->
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Publicaciones", "Ayudas", "Configuración")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ProfileHeader()
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
                2 -> configuracionTab(viewModel)
            }
        }
    }
}

@Composable
fun ProfileHeader() {
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
                    Text(text = "MG", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(text = "María González", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Héroe de Mascotas", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Amante de los animales y voluntaria en refugios locales. Siempre dispuesta a ayudar a reunir mascotas con sus familias.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                Text(text = "Palermo, Buenos Aires", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp), color = Color.White.copy(alpha = 0.9f))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                Text(text = "Miembro desde Enero 2024", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp), color = Color.White.copy(alpha = 0.9f))
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(value = "12", label = "Mascotas Ayudadas")
        StatItem(value = "2,450 HELP", label = "Total Ganado", isHighlighted = true)
        StatItem(value = "8", label = "Publicaciones", isHighlighted = true, highlightColor = Color(0xFFFFA000))
        StatItem(value = "4.8", label = "Calificación")
    }
}

@Composable
fun StatItem(value: String, label: String, isHighlighted: Boolean = false, highlightColor: Color = Color(0xFF00C853)) {
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

fun LazyListScope.publicationsTab() {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Mis Publicaciones", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Button(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Nueva")
            }
        }
    }
    item { PublicationCard("Luna", "Mascota Perdida", "5 días atrás", "234 vistas", "12 respuestas", "Activo") }
    item { PublicationCard("Desconocido", "Mascota Encontrada", "1 semana atrás", "456 vistas", "8 respuestas", "Resuelto") }
}

@Composable
fun PublicationCard(title: String, subtitle: String, date: String, views: String, responses: String, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = "https://i.imgur.com/8zQ2X9C.png",
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
                Text(text = date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = "$views - $responses", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                text = status,
                color = if (status == "Activo") Color.White else Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        color = if (status == "Activo") Color.Red.copy(alpha = 0.8f) else Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

fun LazyListScope.ayudasTab() {
    item {
        Text(
            text = "Mascotas que Ayudé a Encontrar",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        )
    }
    item { HelpedPetCard("Max", "Carlos Ruiz", "500 HELP", "2 semanas atrás • Encontrado") }
    item { HelpedPetCard("Coco", "Ana López", "300 HELP", "1 mes atrás • Información Útil") }
}

@Composable
fun HelpedPetCard(name: String, owner: String, reward: String, details: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Dueño: $owner", style = MaterialTheme.typography.bodyMedium)
                Text(text = details, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = reward, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                Text(text = "Completado", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

fun LazyListScope.configuracionTab(viewModel: ProfileViewModel) {
    item { // Personal Info section
        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(icon = Icons.Default.Email, text = "maria.gonzalez@email.com")
                InfoRow(icon = Icons.Default.Phone, text = "+54 11 1234-5678")
                InfoRow(icon = Icons.Default.LocationOn, text = "Palermo, Buenos Aires")
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // Can't preview this directly anymore as it needs a NavController
    // and a ViewModel. You would need to create a fake ViewModel for the preview.
}
