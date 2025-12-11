
package com.ucb.helpet.features.home.presentation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil3.compose.AsyncImage
import com.google.gson.Gson
import com.ucb.helpet.R
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.profile.presentation.ProfileScreen
import com.ucb.helpet.features.rewards.presentation.RewardsScreen
import com.ucb.helpet.features.search.presentation.SearchScreen
import org.koin.androidx.compose.koinViewModel

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    object Home : BottomNavItem(
        title = "home_bottom_nav_home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    )
    object Search : BottomNavItem(
        title = "home_bottom_nav_search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
    )
    object Rewards : BottomNavItem(
        title = "home_bottom_nav_rewards",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarBorder,
    )
    object Profile : BottomNavItem(
        title = "home_bottom_nav_profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = koinViewModel()) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Rewards,
        BottomNavItem.Profile
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index },
                        label = { Text(text = stringResource(id = R.string::class.java.getField(item.title).getInt(null))) },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                                contentDescription = stringResource(id = R.string::class.java.getField(item.title).getInt(null))
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("report_pet") },
                shape = CircleShape,
                containerColor = Color(0xFF34D399)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.home_fab_add), tint = Color.Black)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItemIndex) {
                0 -> HomeContent(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    onViewAllPetsClick = { selectedItemIndex = 1 },
                    onReportLostClick = { navController.navigate("report_pet") },
                    onSearchPetsClick = { selectedItemIndex = 1 }
                )
                1 -> SearchScreen(navController = navController, viewModel = koinViewModel())
                2 -> RewardsScreen()
                3 -> ProfileScreen(
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onReportPetClick = { navController.navigate("report_pet") },
                    onPetClick = { pet ->
                        val petJson = Uri.encode(Gson().toJson(pet))
                        navController.navigate("pet_detail/$petJson")
                    }
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    homeViewModel: HomeViewModel,
    onViewAllPetsClick: () -> Unit,
    onReportLostClick: () -> Unit,
    onSearchPetsClick: () -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Heart Icon",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.home_title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.home_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onSearchPetsClick() },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.home_search_pets_button))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onReportLostClick() },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Report Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.home_report_lost_button))
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(modifier = Modifier.weight(1f), value = "1,247", label = stringResource(R.string.home_stat_pets_reunited))
                StatCard(modifier = Modifier.weight(1f), value = "45.6", label = stringResource(R.string.home_stat_eth_in_rewards))
                StatCard(modifier = Modifier.weight(1f), value = "892", label = stringResource(R.string.home_stat_active_users))
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_recently_reported_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.home_recently_reported_subtitle),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        when (val state = uiState) {
            is HomeUiState.Loading -> {
                item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
            }
            is HomeUiState.Success -> {
                items(state.pets) { pet ->
                    PetCard(
                        pet = pet,
                        onDetailClick = { selectedPet ->
                            val petJson = Uri.encode(Gson().toJson(selectedPet))
                            navController.navigate("pet_detail/$petJson")
                        }
                    )
                }
            }
            is HomeUiState.Error -> {
                item { Text(text = state.message, color = Color.Red, modifier = Modifier.padding(16.dp)) }
            }
        }

        item {
            OutlinedButton(
                onClick = { onViewAllPetsClick() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(R.string.home_view_all_pets_button))
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.home_how_it_works_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.home_how_it_works_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                HowItWorksStep(
                    icon = { Icon(Icons.Default.Search, "Report") },
                    title = stringResource(R.string.home_how_it_works_step1_title),
                    description = stringResource(R.string.home_how_it_works_step1_desc)
                )
                HowItWorksStep(
                    icon = { Icon(Icons.Default.Person, "Connect") },
                    title = stringResource(R.string.home_how_it_works_step2_title),
                    description = stringResource(R.string.home_how_it_works_step2_desc)
                )
                HowItWorksStep(
                    icon = { Icon(Icons.Default.Star, "Reward") },
                    title = stringResource(R.string.home_how_it_works_step3_title),
                    description = stringResource(R.string.home_how_it_works_step3_desc)
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.home_feature1), modifier = Modifier.padding(4.dp))
                Text(stringResource(R.string.home_feature2), modifier = Modifier.padding(4.dp))
                Text(stringResource(R.string.home_feature3), modifier = Modifier.padding(4.dp))
            }
        }
    }
}


@Composable
fun HowItWorksStep(icon: @Composable () -> Unit, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PetCard(pet: Pet, onDetailClick: (Pet) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pet.imageUrl,
                contentDescription = pet.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = pet.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = pet.type, style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = pet.location, style = MaterialTheme.typography.bodySmall)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = pet.status,
                    color = if (pet.status == "Perdido") Color.Red else Color.Green,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = if (pet.status == "Perdido") Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(onClick = { onDetailClick(pet) }) {
                    Text(text = stringResource(R.string.pet_card_view_details_button))
                }
            }
        }
    }
}
