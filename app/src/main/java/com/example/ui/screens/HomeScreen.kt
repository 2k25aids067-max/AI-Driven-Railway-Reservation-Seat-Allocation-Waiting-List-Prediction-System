package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PurpleTech
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.RailReserveViewModel

@Composable
fun HomeScreen(
    viewModel: RailReserveViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
            .testTag("home_screen")
    ) {
        // Hero Banner with Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.rail_hero_banner),
                contentDescription = "Smart High Speed Railway",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Hero Text
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ElectricCyan.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = "AI-POWERED RESERVATION INTELLIGENCE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "RailReserve AI",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "WL Prediction, Split Booking & Seat Allocation Optimizer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            // One-Line Pitch Callout
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "“Converts waiting-list uncertainty into data-driven confirmation predictions and smarter seat utilization.”",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Judge Pitch & Live Demo Launcher Banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PurpleTech.copy(alpha = 0.12f)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.togglePitchScript(true) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = PurpleTech,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Hackathon Pitch & Demo Script",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "2-minute step-by-step evaluator walkthrough",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = PurpleTech
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SYSTEM MODULES",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Module 0: Search & Book Trains
            FeatureCard(
                title = "Search & Book Trains (AI PRS)",
                subtitle = "Browse trains between any OD stations with real-time class availability, live AI confirmation badges, and smart split-journey recommendations.",
                icon = Icons.Default.Search,
                accentColor = ElectricCyan,
                buttonText = "Search Trains",
                testTag = "open_search_card",
                onClick = { onNavigate(AppScreen.SEARCH_BOOK) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Module 1: Passenger WL Predictor
            FeatureCard(
                title = "Journey 1: Waiting-List Predictor",
                subtitle = "Supports Indian Railways quotas (GNWL, RLWL, PQWL, TQWL, RAC) with calibrated probability gauge, XAI factors, and 4-hour chart prep simulation.",
                icon = Icons.Default.FactCheck,
                accentColor = EmeraldGreen,
                buttonText = "Open WL Predictor",
                testTag = "open_predictor_card",
                onClick = { onNavigate(AppScreen.PREDICTION) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Module 2: Admin Simulation Engine
            FeatureCard(
                title = "Journey 2: Admin Reservation Simulator",
                subtitle = "Compare Baseline FCFS vs AI-Assisted Segment-Aware Allocation across OD pairs. Live occupancy, WL conversion & unused seat reduction metrics.",
                icon = Icons.Default.Analytics,
                accentColor = ElectricCyan,
                buttonText = "Run Allocation Simulation",
                testTag = "open_simulation_card",
                onClick = { onNavigate(AppScreen.SIMULATION) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Module 3: Journey 3 No-Show Reallocation Demo
            FeatureCard(
                title = "Journey 3: Live No-Show Seat Reassignment",
                subtitle = "Simulate intermediate station boarding failure. Real-time priority detection matches eligible RAC/WL passengers and updates coach seating instantly.",
                icon = Icons.Default.AirlineSeatReclineNormal,
                accentColor = Color(0xFFF59E0B),
                buttonText = "Test Live Reassignment Demo",
                testTag = "open_noshow_card",
                onClick = { onNavigate(AppScreen.NO_SHOW_DEMO) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Module 4: Model Performance & Explainability
            FeatureCard(
                title = "ML Model Evaluation & Benchmarking",
                subtitle = "Compare Gradient Boosting vs Random Forest (Fix-Tix baseline) vs Logistic Regression. ROC-AUC 0.892, Brier score 0.114, and confusion matrix.",
                icon = Icons.Default.Psychology,
                accentColor = PurpleTech,
                buttonText = "View AI Architecture & Metrics",
                testTag = "open_evaluation_card",
                onClick = { onNavigate(AppScreen.EVALUATION) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Module 5: System Resilience & Drawback Mitigations
            FeatureCard(
                title = "System Resilience & Drawback Mitigations (12 Fixes)",
                subtitle = "Addresses all 12 operational challenges: uncertainty warnings, offline resilience, non-intrusive CRIS sidecar, sub-15ms Tatkal latency, statutory quota guardrails, DPDP privacy shield, and human overrides.",
                icon = Icons.Default.Shield,
                accentColor = EmeraldGreen,
                buttonText = "Open Resilience Console",
                testTag = "open_resilience_card",
                onClick = { onNavigate(AppScreen.RESILIENCE) }
            )
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    buttonText: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = accentColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
