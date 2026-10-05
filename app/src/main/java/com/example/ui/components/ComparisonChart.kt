package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SimulationComparison
import com.example.ui.theme.BorderSlate
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.RoseRisk

@Composable
fun ComparisonChartCard(
    comparison: SimulationComparison,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("simulation_comparison_card"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OPTIMIZATION COMPARISON",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Identical Demand Dataset",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metric 1: Occupancy Rate
            ComparisonMetricRow(
                title = "Occupancy Rate",
                fcfsValue = "${(comparison.fcfs.occupancyRate * 100).toInt()}%",
                aiValue = "${(comparison.ai.occupancyRate * 100).toInt()}%",
                deltaText = "+${"%.1f".format(comparison.occupancyDelta)}%",
                deltaPositive = true,
                fcfsRatio = comparison.fcfs.occupancyRate,
                aiRatio = comparison.ai.occupancyRate
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Metric 2: WL Conversion Rate
            ComparisonMetricRow(
                title = "Waiting List Conversion",
                fcfsValue = "${(comparison.fcfs.wlConversionRate * 100).toInt()}%",
                aiValue = "${(comparison.ai.wlConversionRate * 100).toInt()}%",
                deltaText = "+${"%.1f".format(comparison.wlConversionDelta)}%",
                deltaPositive = true,
                fcfsRatio = comparison.fcfs.wlConversionRate,
                aiRatio = comparison.ai.wlConversionRate
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Metric 3: Unused Empty Seats
            val maxUnused = (comparison.fcfs.unusedSeats.coerceAtLeast(comparison.ai.unusedSeats) * 1.2f)
            ComparisonMetricRow(
                title = "Unused Vacant Seats",
                fcfsValue = "${comparison.fcfs.unusedSeats} seats",
                aiValue = "${comparison.ai.unusedSeats} seats",
                deltaText = "-${"%.1f".format(comparison.unusedSeatsReduction)}%",
                deltaPositive = true, // Lower unused seats is positive!
                fcfsRatio = (comparison.fcfs.unusedSeats / maxUnused).coerceIn(0f, 1f),
                aiRatio = (comparison.ai.unusedSeats / maxUnused).coerceIn(0f, 1f),
                invertColors = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Metric 4: Estimated Revenue
            val maxRev = comparison.ai.totalRevenue.coerceAtLeast(1.0)
            ComparisonMetricRow(
                title = "Total Journey Revenue",
                fcfsValue = "₹${(comparison.fcfs.totalRevenue / 1000).toInt()}k",
                aiValue = "₹${(comparison.ai.totalRevenue / 1000).toInt()}k",
                deltaText = "+${"%.1f".format(comparison.revenueUpliftPct)}%",
                deltaPositive = true,
                fcfsRatio = (comparison.fcfs.totalRevenue / maxRev).toFloat(),
                aiRatio = 1.0f
            )
        }
    }
}

@Composable
private fun ComparisonMetricRow(
    title: String,
    fcfsValue: String,
    aiValue: String,
    deltaText: String,
    deltaPositive: Boolean,
    fcfsRatio: Float,
    aiRatio: Float,
    invertColors: Boolean = false
) {
    val animatedFcfs by animateFloatAsState(targetValue = fcfsRatio, animationSpec = tween(700), label = "fcfs_bar")
    val animatedAi by animateFloatAsState(targetValue = aiRatio, animationSpec = tween(700), label = "ai_bar")

    val deltaBadgeColor = if (deltaPositive) EmeraldGreen else RoseRisk

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = deltaBadgeColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = deltaText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = deltaBadgeColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // FCFS Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FCFS Baseline",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(90.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BorderSlate.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedFcfs.coerceIn(0.05f, 1f))
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (invertColors) RoseRisk else Color.Gray)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = fcfsValue,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // AI-Assisted Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI-Assisted",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = ElectricCyan,
                modifier = Modifier.width(90.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BorderSlate.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedAi.coerceIn(0.05f, 1f))
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (invertColors) EmeraldGreen else ElectricCyan)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = aiValue,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = if (invertColors) EmeraldGreen else ElectricCyan,
                modifier = Modifier.width(60.dp)
            )
        }
    }
}
