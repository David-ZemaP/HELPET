package com.ucb.helpet.features.login.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucb.helpet.utils.Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = koinViewModel()) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val userType by viewModel.userType.collectAsState()
    val registerState by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = name, onValueChange = { viewModel.name.value = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { viewModel.email.value = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { viewModel.password.value = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            RadioButton(selected = userType == UserType.CLIENT, onClick = { viewModel.userType.value = UserType.CLIENT })
            Text("Client", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = userType == UserType.VETERINARY, onClick = { viewModel.userType.value = UserType.VETERINARY })
            Text("Veterinary", modifier = Modifier.align(Alignment.CenterVertically))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.onRegisterClicked() }) {
            Text("Register")
        }

        when (val state = registerState) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is Resource.Success -> {
                Text("Registration Successful!", color = MaterialTheme.colorScheme.primary)
            }
            is Resource.Error -> {
                Text(state.message ?: "Unknown Error", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
            }
            else -> {}
        }
    }
}
