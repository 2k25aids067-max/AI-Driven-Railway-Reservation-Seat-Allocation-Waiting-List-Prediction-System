package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PurpleTech

@Composable
fun DemoScriptModal(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("pitch_script_modal")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = PurpleTech,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Judge Pitch & Demo Script",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section 1: Opening (30 Seconds)
                ScriptSection(
                    title = "1. Opening (30 Seconds)",
                    badge = "Problem & Pitch",
                    badgeColor = PurpleTech,
                    scriptText = "“Railway passengers do not only need a ticket; they need to know the likelihood that their ticket will confirm. At the same time, operators lose capacity through cancellations, no-shows, and fragmented demand. Our system connects prediction with allocation to improve both passenger transparency and seat utilization.”"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 2: Live Demo Walkthrough (2-3 Minutes)
                ScriptSection(
                    title = "2. Live Demo Flow (2 Minutes)",
                    badge = "In-App Actions",
                    badgeColor = ElectricCyan,
                    scriptText = "• Step 1: Open 'WL Predictor', tap '2847291048 (Howrah Rajdhani WL-14)'. Show the 78% confirmation gauge, position movement of -14 spots, and top contributing factors.\n• Step 2: Open 'Simulator'. Run identical demand on FCFS vs AI-Assisted. Point out the +7% occupancy uplift and 41% reduction in empty seats.\n• Step 3: Open 'Live Demo', tap Berth 1 or 2, click 'Simulate No-Show at Kanpur'. Show real-time re-assignment to RAC-1 and the auditable log entry."
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 3: Technical Explanation (1 Minute)
                ScriptSection(
                    title = "3. Technical Architecture",
                    badge = "Engineering",
                    badgeColor = EmeraldGreen,
                    scriptText = "“Our ML model uses non-linear feature engineering with calibrated probability bands (Brier score 0.128, ROC-AUC 0.874). Our optimization layer solves the segment packing problem, chaining non-overlapping trips into the same physical seat while guaranteeing hard segment capacity limits.”"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section 4: Closing (20 Seconds)
                ScriptSection(
                    title = "4. Closing (20 Seconds)",
                    badge = "Impact",
                    badgeColor = PurpleTech,
                    scriptText = "“Our innovation is not just predicting waiting-list confirmation. It creates an intelligent railway reservation layer that connects prediction, demand forecasting, and constrained seat allocation in one auditable workflow.”"
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Got It – Start Live Demo")
                }
            }
        }
    }
}

@Composable
private fun ScriptSection(
    title: String,
    badge: String,
    badgeColor: androidx.compose.ui.graphics.Color,
    scriptText: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = scriptText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}
