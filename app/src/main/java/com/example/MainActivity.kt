package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoScriptModal
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ModelEvaluationScreen
import com.example.ui.screens.NoShowScreen
import com.example.ui.screens.PredictionScreen
import com.example.ui.screens.ResilienceScreen
import com.example.ui.screens.SearchBookScreen
import com.example.ui.screens.SimulationScreen
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PurpleTech
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.RailReserveViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RailReserveViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val showPitchScript by viewModel.showPitchScript.collectAsState()

                // Hardware back press handling
                BackHandler(enabled = currentScreen != AppScreen.HOME) {
                    viewModel.navigateBack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "RailReserve AI",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            },
                            actions = {
                                IconButton(
                                    onClick = { viewModel.navigateTo(AppScreen.RESILIENCE) },
                                    modifier = Modifier.testTag("resilience_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "System Resilience",
                                        tint = EmeraldGreen
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.togglePitchScript(true) },
                                    modifier = Modifier.testTag("pitch_guide_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RecordVoiceOver,
                                        contentDescription = "Pitch Guide",
                                        tint = PurpleTech
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp,
                            modifier = Modifier.testTag("main_navigation_bar")
                        ) {
                            val items = listOf(
                                Triple(AppScreen.HOME, Icons.Default.Dashboard, "Home"),
                                Triple(AppScreen.SEARCH_BOOK, Icons.Default.Train, "Book"),
                                Triple(AppScreen.PREDICTION, Icons.Default.FactCheck, "Predict"),
                                Triple(AppScreen.SIMULATION, Icons.Default.Analytics, "Sim"),
                                Triple(AppScreen.NO_SHOW_DEMO, Icons.Default.AirlineSeatReclineNormal, "Live"),
                                Triple(AppScreen.RESILIENCE, Icons.Default.Shield, "Resilience"),
                                Triple(AppScreen.EVALUATION, Icons.Default.Psychology, "AI Model")
                            )

                            items.forEach { (screen, icon, label) ->
                                val isSelected = currentScreen == screen
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { viewModel.navigateTo(screen) },
                                    icon = {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = label,
                                            modifier = Modifier.size(19.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 8.5.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            maxLines = 1
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    modifier = Modifier.testTag("nav_${screen.name.lowercase()}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                AppScreen.HOME -> HomeScreen(
                                    viewModel = viewModel,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                                AppScreen.SEARCH_BOOK -> SearchBookScreen(
                                    viewModel = viewModel,
                                    onNavigateToPrediction = { viewModel.navigateTo(AppScreen.PREDICTION) }
                                )
                                AppScreen.PREDICTION -> PredictionScreen(viewModel = viewModel)
                                AppScreen.SIMULATION -> SimulationScreen(viewModel = viewModel)
                                AppScreen.NO_SHOW_DEMO -> NoShowScreen(viewModel = viewModel)
                                AppScreen.RESILIENCE -> ResilienceScreen(viewModel = viewModel)
                                AppScreen.EVALUATION -> ModelEvaluationScreen()
                            }
                        }
                    }

                    if (showPitchScript) {
                        DemoScriptModal(
                            onDismiss = { viewModel.togglePitchScript(false) }
                        )
                    }
                }
            }
        }
    }
}
