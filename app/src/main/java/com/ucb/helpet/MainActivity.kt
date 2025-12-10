package com.ucb.helpet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.ucb.helpet.navigation.AppNavigation
import com.ucb.helpet.ui.theme.HelpetTheme

class MainActivity : ComponentActivity() {

    private var deepLinkIntent by mutableStateOf<Intent?>(null)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        // Intento manual para obtener el token de FCM
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_MANUAL", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_MANUAL", "Manual token: $token")
        }

        if (intent?.hasExtra("petId") == true) {
            deepLinkIntent = intent
        }

        setContent {
            HelpetTheme {
                val navController = rememberNavController()

                LaunchedEffect(deepLinkIntent) {
                    deepLinkIntent?.let { intent ->
                        val petId = intent.getStringExtra("petId")
                        val notificationType = intent.getStringExtra("notificationType")
                        if (!petId.isNullOrEmpty()) {
                            // Aquí puedes agregar lógica para manejar diferentes tipos de notificaciones
                            when (notificationType) {
                                // Por ejemplo:
                                // "ADOPTION_REQUEST" -> navController.navigate("adoption_requests/$petId")
                                else -> navController.navigate("pet_detail_id/$petId")
                            }
                        }

                        deepLinkIntent = null
                    }
                }


                AppNavigation(navController)
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.hasExtra("petId")) {
            deepLinkIntent = intent
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}