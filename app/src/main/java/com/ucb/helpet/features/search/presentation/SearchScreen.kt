package com.ucb.helpet.features.search.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ucb.helpet.features.search.presentation.SearchViewModel
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.presentation.PetCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = koinViewModel()) {
    var showFilters by remember { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val pets by viewModel.pets.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Buscar Mascotas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Encuentra mascotas perdidas o reporta una que hayas encontrado",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Buscar por nombre, raza, ubicación...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = { showFilters = !showFilters }) {
                Text("Filtros")
            }
            Button(onClick = { viewModel.searchPets() }) {
                Text("Buscar")
            }
        }

        if (showFilters) {
            FilterSectionContent() // LLAMADA RENOMBRADA
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Resultados (${pets.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(pets) { pet ->
                SearchPetListItem(pet = pet, onContactClick = {}, onDetailClick = {}) // LLAMADA ACTUALIZADA
            }
        }
    }
}