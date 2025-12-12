package com.ucb.helpet.features.home.presentation.report

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.ucb.helpet.R
import com.ucb.helpet.features.home.domain.model.Pet
import org.koin.androidx.compose.getViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPetScreen(
    navController: NavController,
    viewModel: ReportPetViewModel = getViewModel()
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

    // Image State
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val animalTypes = stringArrayResource(R.array.animal_types).toList()
    val sizes = stringArrayResource(R.array.pet_sizes).toList()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                imageUri = copyUriToMediaStore(context, it)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                // The URI is already available in the imageUri state
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // The camera can now be launched
                val newImageUri = createImageUri(context)
                if (newImageUri != null) {
                    imageUri = newImageUri
                    cameraLauncher.launch(newImageUri)
                }
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Handle Success
    LaunchedEffect(uiState) {
        if (uiState is ReportPetUiState.Success) {
            Toast.makeText(context, context.getString(R.string.report_pet_success_toast), Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            viewModel.resetState()
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Seleccionar fuente de imagen") },
            text = { Text("¿De dónde quieres obtener la imagen?") },
            confirmButton = {
                Button(onClick = {
                    showImageSourceDialog = false
                    imagePickerLauncher.launch("image/*")
                }) {
                    Text("Galería")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showImageSourceDialog = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Cámara")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.report_pet_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.report_pet_back_button), color = MaterialTheme.colorScheme.primary)
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
                Text(stringResource(R.string.report_pet_subtitle), color = Color.Gray, fontSize = 14.sp)
            }

            // 1. Publication Type (Lost vs Found)
            item {
                SectionTitle(stringResource(R.string.report_pet_publication_type_section_title))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SelectionCard(
                        title = stringResource(R.string.report_pet_lost_pet_option),
                        subtitle = stringResource(R.string.report_pet_lost_pet_subtitle),
                        icon = "😢", // You can use a real Icon here
                        isSelected = status == "Perdido",
                        onClick = { status = "Perdido" },
                        modifier = Modifier.weight(1f)
                    )
                    SelectionCard(
                        title = stringResource(R.string.report_pet_found_pet_option),
                        subtitle = stringResource(R.string.report_pet_found_pet_subtitle),
                        icon = "🎉", // Party popper emoji
                        isSelected = status == "Encontrado",
                        onClick = { status = "Encontrado" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 2. Photo Upload
            item {
                SectionTitle(stringResource(R.string.report_pet_photo_section_title))
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
                        .clickable { showImageSourceDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.FileUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(stringResource(R.string.report_pet_upload_photo_placeholder), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // 3. Pet Info
            item {
                CardSection(stringResource(R.string.report_pet_info_section_title)) {
                    FormInput(label = stringResource(R.string.report_pet_name_label), value = name, onValueChange = { name = it }, placeholder = stringResource(R.string.report_pet_name_placeholder))

                    // Type Dropdown
                    ExposedDropdown(
                        label = stringResource(R.string.report_pet_type_label),
                        options = animalTypes,
                        selectedOption = type,
                        onOptionSelected = { type = it },
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = it }
                    )

                    FormInput(label = stringResource(R.string.report_pet_breed_label), value = breed, onValueChange = { breed = it }, placeholder = stringResource(R.string.report_pet_breed_placeholder))
                    FormInput(label = stringResource(R.string.report_pet_color_label), value = color, onValueChange = { color = it }, placeholder = stringResource(R.string.report_pet_color_placeholder))

                    // Size Dropdown
                    ExposedDropdown(
                        label = stringResource(R.string.report_pet_size_label),
                        options = sizes,
                        selectedOption = size,
                        onOptionSelected = { size = it },
                        expanded = sizeExpanded,
                        onExpandedChange = { sizeExpanded = it }
                    )

                    FormInput(label = stringResource(R.string.report_pet_age_label), value = age, onValueChange = { age = it }, placeholder = stringResource(R.string.report_pet_age_placeholder))
                }
            }

            // 4. Location & Date
            item {
                CardSection(stringResource(R.string.report_pet_location_date_section_title)) {
                    FormInput(label = stringResource(R.string.report_pet_last_seen_date_label), value = lastSeenDate, onValueChange = { lastSeenDate = it }, placeholder = stringResource(R.string.report_pet_last_seen_date_placeholder), icon = Icons.Default.CalendarToday)
                    FormInput(label = stringResource(R.string.report_pet_address_label), value = address, onValueChange = { address = it }, placeholder = stringResource(R.string.report_pet_address_placeholder))
                    FormInput(label = stringResource(R.string.report_pet_city_label), value = city, onValueChange = { city = it }, placeholder = stringResource(R.string.report_pet_city_placeholder))
                }
            }

            // 5. Detailed Description
            item {
                CardSection(stringResource(R.string.report_pet_description_section_title)) {
                    FormInput(
                        label = stringResource(R.string.report_pet_description_label),
                        value = description,
                        onValueChange = { description = it },
                        placeholder = stringResource(R.string.report_pet_description_placeholder),
                        minLines = 3
                    )
                    Text(stringResource(R.string.report_pet_description_char_counter), style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    FormInput(
                        label = stringResource(R.string.report_pet_additional_info_label),
                        value = additionalInfo,
                        onValueChange = { additionalInfo = it },
                        placeholder = stringResource(R.string.report_pet_additional_info_placeholder),
                        minLines = 2
                    )
                }
            }

            // 6. Contact Info
            item {
                CardSection(stringResource(R.string.report_pet_contact_info_section_title)) {
                    FormInput(label = stringResource(R.string.report_pet_contact_name_label), value = contactName, onValueChange = { contactName = it }, placeholder = stringResource(R.string.report_pet_contact_name_placeholder))
                    FormInput(label = stringResource(R.string.report_pet_contact_phone_label), value = contactPhone, onValueChange = { contactPhone = it }, placeholder = stringResource(R.string.report_pet_contact_phone_placeholder), keyboardType = KeyboardType.Phone)
                    FormInput(label = stringResource(R.string.report_pet_contact_email_label), value = contactEmail, onValueChange = { contactEmail = it }, placeholder = stringResource(R.string.report_pet_contact_email_placeholder), keyboardType = KeyboardType.Email)
                    Text(stringResource(R.string.report_pet_contact_info_disclaimer), style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                }
            }

            // 7. Reward
            item {
                CardSection(stringResource(R.string.report_pet_reward_section_title), icon = Icons.Default.MonetizationOn) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = hasReward, onCheckedChange = { hasReward = it })
                        Text(stringResource(R.string.report_pet_reward_checkbox_label), style = MaterialTheme.typography.bodyMedium)
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
                        Text(stringResource(R.string.report_pet_cancel_button))
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
                            viewModel.reportPet(pet, imageUri)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = uiState !is ReportPetUiState.Loading
                    ) {
                        if (uiState is ReportPetUiState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(stringResource(R.string.report_pet_publish_button))
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// --- FILE UTILITY FUNCTIONS -- -

private fun createImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        put(MediaStore.MediaColumns.DISPLAY_NAME, "JPEG_${timeStamp}_.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

private fun copyUriToMediaStore(context: Context, uri: Uri): Uri? {
    val newUri = createImageUri(context)
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            context.contentResolver.openOutputStream(newUri!!).use { outputStream ->
                inputStream.copyTo(outputStream!!)
            }
        }
        newUri
    } catch (e: Exception) {
        e.printStackTrace()
        // If the copy fails, delete the created URI entry
        newUri?.let { context.contentResolver.delete(it, null, null) }
        null
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
                value = if (selectedOption.isEmpty()) stringResource(R.string.register_user_type_placeholder) else selectedOption,
                onValueChange = { },
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
