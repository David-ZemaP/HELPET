package com.ucb.helpet.features.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterSectionContent() { // RENOMBRADA
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        // Dropdown for pet type
        var petType by remember { mutableStateOf("") }
        OutlinedTextField(
            value = petType,
            onValueChange = { petType = it },
            label = { Text("Tipo de Mascota") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for status
        var status by remember { mutableStateOf("") }
        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for location
        var location by remember { mutableStateOf("") }
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
