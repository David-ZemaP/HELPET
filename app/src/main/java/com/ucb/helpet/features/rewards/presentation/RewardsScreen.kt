package com.ucb.helpet.features.rewards.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ucb.helpet.R
import org.koin.androidx.compose.koinViewModel

data class RewardPet(
    val name: String,
    val description: String,
    val location: String,
    val lostTime: String,
    val rewardAmount: Int,
    val imageUrl: String
)

data class RewardHistoryItem(
    val name: String,
    val description: String,
    val date: String,
    val amount: Int,
    val status: String
)

data class Achievement(
    val title: String,
    val description: String,
)

@Composable
fun RewardsScreen(viewModel: RewardsViewModel = koinViewModel()) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.rewards_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.rewards_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = stringResource(R.string.rewards_balance_label),
                    value = "1,200",
                    unit = stringResource(R.string.rewards_token_symbol),
                    icon = Icons.Default.AccountBalanceWallet,
                    backgroundColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = stringResource(R.string.rewards_total_label),
                    value = "2,450",
                    unit = stringResource(R.string.rewards_token_symbol),
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    backgroundColor = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = stringResource(R.string.rewards_helped_label),
                    value = "12",
                    unit = stringResource(R.string.rewards_pets_unit),
                    icon = Icons.Default.Pets,
                    backgroundColor = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = stringResource(R.string.rewards_rank_label),
                    value = stringResource(R.string.rewards_rank_hero),
                    unit = "",
                    icon = Icons.Default.EmojiEvents,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            RewardsTabs(selectedTabIndex = selectedTabIndex, onTabSelected = { selectedTabIndex = it })
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (selectedTabIndex) {
            0 -> {
                when (val state = uiState) {
                    is RewardsUiState.Loading -> {
                        item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                    }
                    is RewardsUiState.Success -> {
                        items(state.rewards) { pet ->
                            RewardPetCard(pet = pet)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    is RewardsUiState.Error -> {
                        item { Text(text = state.message, color = Color.Red, modifier = Modifier.padding(16.dp)) }
                    }
                }
            }
            1 -> {
                item { HistoryContent() }
            }
            2 -> {
                item { AchievementsContent() }
            }
            3 -> {
                item { WalletContent() }
            }
        }
    }
}

@Composable
fun HistoryContent() {
    val historyItems = listOf(
        RewardHistoryItem("Luna", stringResource(R.string.rewards_history_found_pet), "2 días atrás", 500, stringResource(R.string.rewards_history_status_completed)),
        RewardHistoryItem("Michi", stringResource(R.string.rewards_history_useful_info), "1 semana atrás", 300, stringResource(R.string.rewards_history_status_completed)),
        RewardHistoryItem("Bobby", stringResource(R.string.rewards_history_found_pet), "2 semanas atrás", 800, stringResource(R.string.rewards_history_status_completed))
    )

    Column {
        Text(
            text = stringResource(R.string.rewards_history_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        historyItems.forEach { item ->
            HistoryItemCard(item = item)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun AchievementsContent() {
    val achievements = listOf(
        Achievement(stringResource(R.string.achievement_first_help_title), stringResource(R.string.achievement_first_help_desc)),
        Achievement(stringResource(R.string.achievement_detective_title), stringResource(R.string.achievement_detective_desc)),
        Achievement(stringResource(R.string.achievement_hero_title), stringResource(R.string.achievement_hero_desc)),
        Achievement(stringResource(R.string.achievement_legend_title), stringResource(R.string.achievement_legend_desc))
    )

    Column {
        Text(
            text = stringResource(R.string.rewards_achievements_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        achievements.forEach { achievement ->
            AchievementCard(achievement = achievement)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun WalletContent() {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.rewards_wallet_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.rewards_wallet_total_balance),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "1,200 HELP",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.rewards_wallet_address_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.rewards_wallet_address_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.rewards_wallet_withdraw_button))
                    }
                    OutlinedButton(onClick = { /* TODO */ }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.rewards_wallet_transfer_button))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.rewards_wallet_stats_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TokenStat(label = stringResource(R.string.rewards_wallet_stats_tokens_this_month), value = "850 HELP")
            TokenStat(label = stringResource(R.string.rewards_wallet_stats_avg_per_pet), value = "425 HELP")
            TokenStat(label = stringResource(R.string.rewards_wallet_stats_best_reward), value = "800 HELP")
        }
    }
}

@Composable
fun TokenStat(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}


@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(achievement.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(achievement.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


@Composable
fun StatCard(title: String, value: String, unit: String, icon: ImageVector, backgroundColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                Icon(imageVector = icon, contentDescription = null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(20.dp))
            }
            Column {
                Text(text = value, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                if (unit.isNotEmpty()) {
                    Text(text = unit, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RewardsTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf(
        stringResource(R.string.rewards_tab_available),
        stringResource(R.string.rewards_tab_history),
        stringResource(R.string.rewards_tab_achievements),
        stringResource(R.string.rewards_tab_wallet)
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        edgePadding = 0.dp,
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selectedTabIndex == index) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                Text(
                    text = title,
                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun RewardPetCard(pet: RewardPet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            AsyncImage(
                model = pet.imageUrl,
                contentDescription = pet.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = pet.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Reward", tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = pet.rewardAmount.toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = pet.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = pet.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(" • ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = pet.lostTime, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.rewards_help_find_button))
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: RewardHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(item.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(" • ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(item.date, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("+" + item.amount, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
                Text(stringResource(R.string.rewards_token_symbol), color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.status, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
