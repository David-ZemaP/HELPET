package com.ucb.helpet.features.home.presentation.report

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ucb.helpet.features.home.domain.model.Pet
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPetScreen(
    navController: NavController,
    viewModel: ReportPetViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- Form States ---
    var status by remember { mutableStateOf("Perdido") } // "Perdido" or "Encontrado"
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    // Location & Date
    var lastSeenDate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // Description
    var description by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    // Contact
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }

    // Reward
    var hasReward by remember { mutableStateOf(false) }

    // Dropdown States
    var typeExpanded by remember { mutableStateOf(false) }
    var sizeExpanded by remember { mutableStateOf(false) }

    val animalTypes = listOf("Perro", "Gato", "Ave", "Otro")
    val sizes = listOf("Pequeño", "Mediano", "Grande")

    // Handle Success
    LaunchedEffect(uiState) {
        if (uiState is ReportPetUiState.Success) {
            Toast.makeText(context, "Publicación creada exitosamente", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Publicación", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Volver", color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Completa la información para ayudarnos a encontrar a la mascota", color = Color.Gray, fontSize = 14.sp)
            }

            // 1. Publication Type (Lost vs Found)
            item {
                SectionTitle("Tipo de Publicación")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SelectionCard(
                        title = "Mascota Perdida",
                        subtitle = "Busco a mi mascota",
                        icon = "😢", // You can use a real Icon here
                        isSelected = status == "Perdido",
                        onClick = { status = "Perdido" },
                        modifier = Modifier.weight(1f)
                    )
                    SelectionCard(
                        title = "Mascota Encontrada",
                        subtitle = "Encontré una mascota",
                        icon = "\uD83C\uDF89", // Party popper emoji
                        isSelected = status == "Encontrado",
                        onClick = { status = "Encontrado" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 2. Photo Upload
            item {
                SectionTitle("Foto de la Mascota *")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { /* TODO: Implement Image Picker */ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.FileUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sube una foto clara", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // 3. Pet Info
            item {
                CardSection("Información de la Mascota") {
                    FormInput(label = "Nombre *", value = name, onValueChange = { name = it }, placeholder = "Ej: Max")

                    // Type Dropdown
                    ExposedDropdown(
                        label = "Tipo de Animal *",
                        options = animalTypes,
                        selectedOption = type,
                        onOptionSelected = { type = it },
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = it }
                    )

                    FormInput(label = "Raza", value = breed, onValueChange = { breed = it }, placeholder = "Ej: Labrador, Mestizo")
                    FormInput(label = "Color Principal", value = color, onValueChange = { color = it }, placeholder = "Ej: Negro, Marrón")

                    // Size Dropdown
                    ExposedDropdown(
                        label = "Tamaño",
                        options = sizes,
                        selectedOption = size,
                        onOptionSelected = { size = it },
                        expanded = sizeExpanded,
                        onExpandedChange = { sizeExpanded = it }
                    )

                    FormInput(label = "Edad Aproximada", value = age, onValueChange = { age = it }, placeholder = "Ej: 2 años, Cachorro")
                }
            }

            // 4. Location & Date
            item {
                CardSection("Ubicación y Fecha") {
                    FormInput(label = "Última vez visto *", value = lastSeenDate, onValueChange = { lastSeenDate = it }, placeholder = "dd/mm/aaaa", icon = Icons.Default.CalendarToday)
                    FormInput(label = "Dirección/Zona *", value = address, onValueChange = { address = it }, placeholder = "Ej: Av. Santa Fe 1234")
                    FormInput(label = "Ciudad *", value = city, onValueChange = { city = it }, placeholder = "Ej: Buenos Aires")
                }
            }

            // 5. Detailed Description
            item {
                CardSection("Descripción Detallada") {
                    FormInput(
                        label = "Descripción *",
                        value = description,
                        onValueChange = { description = it },
                        placeholder = "Describe características distintivas...",
                        minLines = 3
                    )
                    Text("0 caracteres (mínimo 20)", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    FormInput(
                        label = "Información Adicional (Opcional)",
                        value = additionalInfo,
                        onValueChange = { additionalInfo = it },
                        placeholder = "Collar, chip, condición de salud...",
                        minLines = 2
                    )
                }
            }

            // 6. Contact Info
            item {
                CardSection("Información de Contacto") {
                    FormInput(label = "Nombre", value = contactName, onValueChange = { contactName = it }, placeholder = "Tu nombre")
                    FormInput(label = "Teléfono", value = contactPhone, onValueChange = { contactPhone = it }, placeholder = "+54 9 11 ...", keyboardType = KeyboardType.Phone)
                    FormInput(label = "Email", value = contactEmail, onValueChange = { contactEmail = it }, placeholder = "correo@ejemplo.com", keyboardType = KeyboardType.Email)
                    Text("* Al menos un método de contacto es requerido", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                }
            }

            // 7. Reward
            item {
                CardSection("Recompensa (Opcional)", icon = Icons.Default.MonetizationOn) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = hasReward, onCheckedChange = { hasReward = it })
                        Text("Quiero ofrecer una recompensa por encontrar a mi mascota", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Error Message
            if (uiState is ReportPetUiState.Error) {
                item {
                    Text(
                        text = (uiState as ReportPetUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // 8. Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val pet = Pet(
                                status = status,
                                name = name,
                                type = type,
                                breed = breed,
                                color = color,
                                size = size,
                                age = age,
                                lastSeenDate = lastSeenDate,
                                location = address,
                                city = city,
                                description = description,
                                additionalInfo = additionalInfo,
                                contactName = contactName,
                                contactPhone = contactPhone,
                                contactEmail = contactEmail,
                                hasReward = hasReward
                            )
                            viewModel.reportPet(pet)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = uiState !is ReportPetUiState.Loading
                    ) {
                        if (uiState is ReportPetUiState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Publicar")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun SectionTitle(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        // You can add an icon here if needed based on context
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun CardSection(title: String, icon: ImageVector? = null, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            content()
        }
    }
}

@Composable
fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector? = null,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            trailingIcon = if (icon != null) { { Icon(icon, contentDescription = null) } } else null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = if (selectedOption.isEmpty()) "Selecciona" else selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectionCard(
    title: String,
    subtitle: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    // If selected, you might want a slight background tint
    // val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent // Optional

    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(if(isSelected) 2.dp else 1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Keep transparent to show background or set specific
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray, lineHeight = 14.sp)
        }
    }
}