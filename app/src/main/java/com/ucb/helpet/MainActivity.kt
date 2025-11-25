package com.ucb.helpet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
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

        if (intent?.hasExtra("petId") == true) {
            deepLinkIntent = intent
        }

        setContent {
            HelpetTheme {
                val navController = rememberNavController()

                LaunchedEffect(deepLinkIntent) {
                    deepLinkIntent?.let { intent ->
                        val petId = intent.getStringExtra("petId")
                        if (!petId.isNullOrEmpty()) {

                            navController.navigate("pet_detail_id/$petId")
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